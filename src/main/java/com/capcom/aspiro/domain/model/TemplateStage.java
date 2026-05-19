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
@Table(name = "template_stage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer orderNumber;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    @ToString.Exclude
    private Template template;

    @OneToMany(mappedBy = "templateStage")
    @ToString.Exclude
    @Builder.Default
    private List<TemplateTask> templateTasks = new ArrayList<>();
}