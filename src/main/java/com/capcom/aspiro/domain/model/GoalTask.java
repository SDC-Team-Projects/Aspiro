package com.capcom.aspiro.domain.model;

import java.time.LocalDate;

import com.capcom.aspiro.domain.model.enums.TaskStatus;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "goal_task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer orderNumber;

    @ManyToOne
    @JoinColumn(name = "goal_stage_id", nullable = false)
    @ToString.Exclude
    private GoalStage goalStage;

    @ManyToOne
    @JoinColumn(name = "template_task_id", nullable = false)
    @ToString.Exclude
    private TemplateTask templateTask;
}