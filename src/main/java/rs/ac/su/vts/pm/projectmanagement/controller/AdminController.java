package rs.ac.su.vts.pm.projectmanagement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@Tag(name = "Admin Controller", description = "Set of endpoints for Application Administration.")
@Slf4j
public class AdminController
{

}
