package com.aarete.pi.repository;

import com.aarete.pi.entity.ClaimLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimLineRepository extends RevisionRepository<ClaimLineEntity,Long,Long>, JpaRepository<ClaimLineEntity, Long> {

}