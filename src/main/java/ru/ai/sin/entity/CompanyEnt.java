package ru.ai.sin.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.entity.model.TimeStamped;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<CompanyStudentEnt> students = new ArrayList<>();
}
