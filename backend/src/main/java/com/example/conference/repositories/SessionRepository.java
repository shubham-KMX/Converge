package com.example.conference.repositories;

import com.example.conference.domain.entities.Session;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

  List<Session> findByConferenceId(UUID conferenceId);

  Optional<Session> findByIdAndConferenceId(UUID id, UUID conferenceId);
}
