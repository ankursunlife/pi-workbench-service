package com.aarete.pi.repository;

import com.aarete.pi.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<StateEntity, String> {
}
