package com.capcom.aspiro.domain.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "goal_stage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer orderNumber;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    @ToString.Exclude
    private Goal goal;

    @ManyToOne
    @JoinColumn(name = "template_stage_id", nullable = false)
    @ToString.Exclude
    private TemplateStage templateStage;

    @OneToMany(mappedBy = "goalStage")
    @ToString.Exclude
    @Builder.Default
    private List<GoalTask> goalTasks = new ArrayList<>();
}