package com.aarete.pi.repository;

import com.aarete.pi.entity.ClaimlineCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimLineCommentRepository extends JpaRepository<ClaimlineCommentEntity, Long> {
}