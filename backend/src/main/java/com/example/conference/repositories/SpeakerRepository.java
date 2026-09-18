package com.example.conference.repositories;

import com.example.conference.domain.entities.Speaker;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, UUID> {
}
