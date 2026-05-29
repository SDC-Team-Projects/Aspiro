package com.capcom.aspiro.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.User;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUser(User user);

    List<Goal> findByUserId(Long userId);

    Optional<Goal> findByIdAndUserEmail(Long id, String email);
}