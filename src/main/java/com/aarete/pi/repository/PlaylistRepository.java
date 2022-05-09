package com.aarete.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aarete.pi.entity.PlaylistEntity;

@Repository
public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {

}
