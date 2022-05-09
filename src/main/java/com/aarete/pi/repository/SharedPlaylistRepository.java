package com.aarete.pi.repository;

import com.aarete.pi.entity.SharedPlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedPlaylistRepository extends JpaRepository<SharedPlaylistEntity, Long> {

}
