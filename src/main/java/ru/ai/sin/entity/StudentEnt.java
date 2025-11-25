package ru.ai.sin.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.*;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import ru.ai.sin.entity.converter.BusynessEnumConverter;
import ru.ai.sin.entity.converter.CourseEnumConverter;
import ru.ai.sin.entity.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "students")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnt {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String city;

    private String hhLink;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(columnDefinition = "TEXT")
    @Basic(fetch = FetchType.LAZY)
    @Size(max = 2000, message = "Additional info must be less than 2000 characters")
    private String bio;

    @Column(unique = true)
    private String imagePath;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Column(name = "course", length = 16)
    @Convert(converter = CourseEnumConverter.class)
    private CourseEnum course;

    @Column(name = "busyness", length = 32)
    @Convert(converter = BusynessEnumConverter.class)
    private BusynessEnum busyness;

    @Embedded
    private UserInformation userInformation = new UserInformation();

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @Embedded
    private ContactInformation contactInformation = new ContactInformation();

    @OneToMany(mappedBy = "student")
    private List<PortfolioEnt> portfolio = new ArrayList<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<InstitutionEnt> education = new ArrayList<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<ExperienceEnt> companies = new ArrayList<>();
}
