package com.capcom.aspiro.domain.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "template_task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer orderNumber;

    @Column(nullable = false)
    private Integer daysOffset;

    @Column(nullable = false)
    private Integer durationDays;

    @ManyToOne
    @JoinColumn(name = "template_stage_id", nullable = false)
    @ToString.Exclude
    private TemplateStage templateStage;
}