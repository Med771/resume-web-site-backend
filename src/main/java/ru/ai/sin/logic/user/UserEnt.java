package ru.ai.sin.logic.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.models.enums.convertor.RoleEnumConverter;
import ru.ai.sin.models.enums.RoleEnum;
import ru.ai.sin.models.embeddables.UserInformation;

import java.util.UUID;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEnt {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(name = "role", length = 32)
    @Convert(converter = RoleEnumConverter.class)
    private RoleEnum role;

    @Embedded
    private UserInformation userInformation = new UserInformation();

    public UserEnt(RoleEnum role, String username, String passwordHash) {
        this.role = role;

        this.userInformation.setUsername(username);
        this.userInformation.setPasswordHash(passwordHash);
    }
}
