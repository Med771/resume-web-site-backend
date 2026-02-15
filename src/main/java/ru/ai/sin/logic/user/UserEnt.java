package ru.ai.sin.logic.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.models.enums.convertor.RoleEnumConverter;
import ru.ai.sin.models.enums.RoleEnum;

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

    private String name;

    @Column(length = 64, unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,64}$", message = "Username must be 3-64 characters, letters, digits or _")
    private String username;

    @Column(name = "password_hash", length = 128)
    private String passwordHash;

    public UserEnt(RoleEnum role, String username, String passwordHash) {
        this.role = role;
        this.setUsername(username);
        this.setPasswordHash(passwordHash);
    }

    public UserEnt(RoleEnum role, String name, String username, String passwordHash) {
        this.role = role;
        this.name = name;
        this.setUsername(username);
        this.setPasswordHash(passwordHash);
    }
}
