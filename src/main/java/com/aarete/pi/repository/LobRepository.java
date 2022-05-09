package com.aarete.pi.repository;

import com.aarete.pi.entity.LobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LobRepository extends JpaRepository<LobEntity, String> {
}
