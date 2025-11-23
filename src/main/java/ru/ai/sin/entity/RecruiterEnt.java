package ru.ai.sin.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import ru.ai.sin.entity.model.ContactInformation;
import ru.ai.sin.entity.model.TimeStamped;
import ru.ai.sin.entity.model.UserInformation;

import java.util.UUID;

@Entity
@Table(name = "employees")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterEnt {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Embedded
    private UserInformation userInformation = new UserInformation();

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @Embedded
    private ContactInformation contactInformation = new ContactInformation();
}
