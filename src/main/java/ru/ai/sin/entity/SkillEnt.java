package ru.ai.sin.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.entity.model.TimeStamped;

@Entity
@Table(name = "skills")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 128)
    private String name;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEnt category;

}
