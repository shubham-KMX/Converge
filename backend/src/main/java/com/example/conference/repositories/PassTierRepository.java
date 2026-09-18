package com.example.conference.repositories;

import com.example.conference.domain.entities.PassTier;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PassTierRepository extends JpaRepository<PassTier, UUID> {

  @Query("SELECT pt FROM PassTier pt WHERE pt.id = :id")
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<PassTier> findByIdWithLock(@Param("id") UUID id);
}
