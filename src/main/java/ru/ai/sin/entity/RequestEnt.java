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
@Table(name = "requests")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", length = 16)
    private String chatId;

    @Column(name = "result", length = 16, nullable = false)
    @Convert(converter = ResultEnumConverter.class)
    private ResultEnum result = ResultEnum.CREATION;

    @Column(name = "chat_title")
    private String chatTitle;

    @Column(name = "chat_url")
    private String chatUrl;

    @Embedded
    private TimeStamped timestamps = new TimeStamped();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private RecruiterEnt recruiter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEnt student;
}
