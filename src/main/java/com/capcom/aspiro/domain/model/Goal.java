package com.capcom.aspiro.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.capcom.aspiro.domain.model.enums.GoalStatus;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "goal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    @ToString.Exclude
    private Template template;

    @OneToMany(mappedBy = "goal")
    @ToString.Exclude
    @Builder.Default
    private List<GoalStage> goalStages = new ArrayList<>();
}