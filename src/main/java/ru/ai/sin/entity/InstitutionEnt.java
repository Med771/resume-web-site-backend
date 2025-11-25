package ru.ai.sin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.entity.model.TimeStamped;

@Entity
@Table(name = "educations_students")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_year", nullable = false)
    @Min(1900)
    @Max(2100)
    private int startYear;

    @Column(name = "end_year", nullable = false)
    @Min(1900)
    @Max(2100)
    private int endYear;

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @ManyToOne
    @JoinColumn(name = "education_id")
    private EducationEnt education;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEnt student;
}
