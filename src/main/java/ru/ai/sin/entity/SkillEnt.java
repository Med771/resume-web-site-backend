package ru.ai.sin.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.entity.join.SpecialitySkillEnt;
import ru.ai.sin.entity.model.TimeStamped;

import java.util.HashSet;
import java.util.Set;

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

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    Set<SpecialitySkillEnt> specialities = new HashSet<>();

    public SkillEnt(String name) {
        this.name = name;
    }
}
