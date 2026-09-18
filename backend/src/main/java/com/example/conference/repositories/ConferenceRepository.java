package com.example.conference.repositories;

import com.example.conference.domain.entities.Conference;
import com.example.conference.domain.entities.ConferenceStatusEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, UUID> {

  Page<Conference> findByOrganizerId(UUID organizerId, Pageable pageable);

  Optional<Conference> findByIdAndOrganizerId(UUID id, UUID organizerId);

  Page<Conference> findByStatus(ConferenceStatusEnum status, Pageable pageable);

  Optional<Conference> findByIdAndStatus(UUID id, ConferenceStatusEnum status);

  @Query(
      value = "SELECT * FROM conferences c WHERE c.status = 'PUBLISHED' AND "
          + "to_tsvector('english', c.name || ' ' || c.venue) @@ plainto_tsquery('english', :searchTerm)",
      countQuery = "SELECT count(*) FROM conferences c WHERE c.status = 'PUBLISHED' AND "
          + "to_tsvector('english', c.name || ' ' || c.venue) @@ plainto_tsquery('english', :searchTerm)",
      nativeQuery = true
  )
  Page<Conference> searchConferences(@Param("searchTerm") String searchTerm, Pageable pageable);
}
