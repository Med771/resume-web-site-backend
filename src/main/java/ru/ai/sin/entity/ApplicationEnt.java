package ru.ai.sin.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ai.sin.entity.converter.ResultEnumConverter;
import ru.ai.sin.entity.model.ResultEnum;
import ru.ai.sin.entity.model.TimeStamped;

@Entity
@Table(name = "applications")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", unique = true, length = 32)
    private String chatId;

    @Column(name = "history_path", unique = true)
    private String historyPath;

    @Column(name = "result", length = 32)
    @Convert(converter = ResultEnumConverter.class)
    private ResultEnum result;

    @Column(name = "result_message")
    private String resultMessage;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private RecruiterEnt recruiter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentEnt student;
}
