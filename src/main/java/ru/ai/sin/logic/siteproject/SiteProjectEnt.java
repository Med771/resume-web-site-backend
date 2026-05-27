package ru.ai.sin.logic.siteproject;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import ru.ai.sin.logic.student.StudentEnt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.models.embeddables.TimeStamped;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "site_projects")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class SiteProjectEnt {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String imagePath;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "visible_to_anonymous", nullable = false)
    private boolean visibleToAnonymous;

    @Column(name = "published_from")
    private LocalDateTime publishedFrom;

    @Column(name = "published_to")
    private LocalDateTime publishedTo;

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "site_project_students",
            joinColumns = @JoinColumn(name = "site_project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id")
    )
    private Set<StudentEnt> students = new HashSet<>();
}
