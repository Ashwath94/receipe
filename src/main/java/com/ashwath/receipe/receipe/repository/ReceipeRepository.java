package com.ashwath.receipe.receipe.repository;

import com.ashwath.receipe.receipe.dto.Receipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceipeRepository extends JpaRepository<Receipe, Long> {
}
