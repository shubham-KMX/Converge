package com.example.conference.repositories;

import com.example.conference.domain.entities.CheckIn;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, UUID> {
}
