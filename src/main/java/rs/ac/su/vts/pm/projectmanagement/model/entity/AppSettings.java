package rs.ac.su.vts.pm.projectmanagement.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import rs.ac.su.vts.pm.projectmanagement.model.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "app_settings")
@Table(name = "app_settings")
public class AppSettings
        extends BaseEntity
{

    private String organization;

    @Column(length = 16)
    private String textSize;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Builder.Default
    private boolean notifications = true;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Builder.Default
    private boolean setupCompleted = false;

    // region hash code, equals, to string
    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppSettings)) {
            return false;
        }
        AppSettings other = (AppSettings) o;
        return Objects.equals(other.getId(), getId());
    }

    @Override
    public int hashCode()
    {
        return 2;
    }
    // endregion
}
