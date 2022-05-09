package com.aarete.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aarete.pi.entity.UserEntity;

@Repository
public interface MasterTableUserRepository extends JpaRepository<UserEntity,Long>{

}
