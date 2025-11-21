package ru.ai.sin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.entity.model.TimeStamped;

@Entity
@Table(name = "portfolio")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 128, nullable = false)
    private String name;

    @Column(nullable = false)
    private String link;

    @Column(name = "additional_info", columnDefinition = "TEXT")
    @Basic(fetch = FetchType.LAZY)
    @Size(max = 2000, message = "Additional info must be less than 2000 characters")
    private String additionalInfo;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEnt student;
}
