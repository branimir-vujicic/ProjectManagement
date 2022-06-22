package rs.ac.su.vts.pm.projectmanagement.services;

import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import rs.ac.su.vts.pm.projectmanagement.exception.*;
import rs.ac.su.vts.pm.projectmanagement.model.common.UserRole;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.*;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.repository.UserRepository;
import rs.ac.su.vts.pm.projectmanagement.services.auth.ContextService;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.*;

@Component
@Slf4j
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final Long recoverPasswordMaxTime;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public UserService(@Value("${user.recoverPassword.maxTime}") Long recoverPasswordMaxTime,
                       UserRepository userRepository, EntityManager entityManager,
                       PasswordEncoder passwordEncoder) {
        this.recoverPasswordMaxTime = recoverPasswordMaxTime;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsername(String username) {
        return userRepository.findOneByEmail(username);
    }

    public User getUserByUsernameRequired(String username) {
        User userByUsername = getUserByUsername(username);
        if (userByUsername == null) {
            throw new ValidationException("user does not exist");
        }
        return userByUsername;
    }

    public User addUser(UserCreateRequest request) {
        if (getUserByUsername(request.getEmail()) != null) {
            throw new UsernameExistsException();
        }

        final User user = UserMapper.INSTANCE.from(request);
        if (CollectionUtils.isEmpty(user.getRoles())) {
            user.setRoles(Collections.singleton(UserRole.USER));
        }

        String key = UUID.randomUUID().toString();
        Long passwordRecoveryTime = calculatePasswordRecoveryTime();
        user.setPasswordRecoveryKey(key);
        user.setPasswordRecoveryTime(passwordRecoveryTime);
        return userRepository.save(user);
    }

    public User updateUser(Long id, @Valid UserUpdateRequest updateRequest) {
        User userById = getUserById(id);
        userById.setName(updateRequest.getName());
        if (StringUtils.hasText(updateRequest.getEmail())) {
            userById.setEmail(updateRequest.getEmail());
        }
        // current user cannot change own roles
        Optional<User> loggedUser = ContextService.getLoggedUser();
        if (loggedUser.isPresent() && !Objects.equals(loggedUser.get().getId(), id)) {
            if (!CollectionUtils.isEmpty(updateRequest.getRoles())) {
                userById.getRoles().clear();
                userById.getRoles().addAll(updateRequest.getRoles());
            }
        }
        return userRepository.save(userById);
    }

    public void enableUser(@Valid Long id) {
        User user = getUserById(id);
        if (!user.isActive()) {
            user.setActive(true);
            userRepository.save(user);
        }
    }

    public void disableUser(@Valid Long id) {
        User user = getUserById(id);
        if (user.isActive()) {
            user.setActive(false);
            userRepository.save(user);
        }
    }

    public void deleteUser(@Valid Long id) {
        User user = getUserById(id);
        Optional<User> loggedUser = ContextService.getLoggedUser();
        if (loggedUser.isPresent()) {
            if (!Objects.equals(loggedUser.get(), user)) {
                user.setDeleted(true);
                userRepository.save(user);
            }
        }
    }

    public Page<User> listUsers(UserListRequest request) {
        BooleanBuilder predicate = request.predicate();
        return userRepository.findAll(predicate, request.pageable());
    }

    public void createPassword(CreatePasswordRequest createPasswordRequest) {
        String password = createPasswordRequest.getPassword();
        String repeatPassword = createPasswordRequest.getConfirmedPassword();
        if (!Objects.equals(password, repeatPassword)) {
            throw new PasswordsDoesntException("Passwords doesn't match");
        }
        User user = userRepository.findOneByEmail(createPasswordRequest.getEmail());
        if (user.getPasswordRecoveryKey() == null) {
            throw new ValidationException("Key doesn't match");
        }
        if (!Objects.equals(createPasswordRequest.getKey(), user.getPasswordRecoveryKey())) {
            throw new ValidationException("Key doesn't match");
        }
        if (user.getPasswordRecoveryTime() == null) {
            throw new ValidationException("Time expired");
        }
        if (System.currentTimeMillis() > user.getPasswordRecoveryTime()) {
            throw new ValidationException("Time expired");
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordRecoveryKey(null);
        user.setPasswordRecoveryTime(null);
        userRepository.save(user);
    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = getUserByUsernameRequired(forgotPasswordRequest.getEmail());
        String key = UUID.randomUUID().toString();
        Long passwordRecoveryTime = calculatePasswordRecoveryTime();
        user.setPasswordRecoveryKey(key);
        user.setPasswordRecoveryTime(passwordRecoveryTime);
        final User savedUser = userRepository.save(user);
//		mailService.sendForgotPasswordEmail(savedUser);
    }

    private long calculatePasswordRecoveryTime() {
        return System.currentTimeMillis() + recoverPasswordMaxTime;
    }

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = ContextService.getLoggedUser().orElseThrow(NotAuthorizedException::new);
        String oldPassword = updatePasswordRequest.getOldPassword();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Bad password or username");
        }
        String password = updatePasswordRequest.getPassword();
        String repeatPassword = updatePasswordRequest.getConfirmedPassword();
        if (!Objects.equals(password, repeatPassword)) {
            throw new PasswordsDoesntException("Passwords doesn't match");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public boolean userExists(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public void changePassword(String username, String oldPassword, String newPassword)
            throws NotAuthorizedException {
        User user = getUserByUsernameRequired(username);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Bad password or username");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User not found: %s", id)));
    }

    public List<User> findAllFilter(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedUserFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<User> users = userRepository.findAll();
        session.disableFilter("deletedUserFilter");
        return users;
    }

    public List<User> listMembersByProject(Long projectId) {
        return userRepository.findAllByProjectMembersProjectId(projectId);
    }

    public List<User> listOrganizersByProject(Long projectId) {
        return userRepository.findAllByProjectOrganizersProjectId(projectId);
    }

    public User getLoggedUser() {
        Optional<User> optionalUser = ContextService.getLoggedUser();
        if (optionalUser.isPresent()) {
            return getUserByUsernameRequired(optionalUser.get().getEmail());
        } else throw new NotFoundException("User doesn't exist!");
    }
}

