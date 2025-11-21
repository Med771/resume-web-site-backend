package ru.ai.sin.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.entity.model.TimeStamped;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "education")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EducationEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String institution;

    @Column(name = "additional_info", columnDefinition = "TEXT")
    @Basic(fetch = FetchType.LAZY)
    @Size(max = 2000, message = "Additional info must be less than 2000 characters")
    private String additionalInfo;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @OneToMany(mappedBy = "education", fetch = FetchType.LAZY)
    private List<EducationStudentEnt> education = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_id")
    private List<SkillEnt> skills = new ArrayList<>();
}
