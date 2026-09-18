package com.example.conference.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "conferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conference {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "conference_start")
  private LocalDateTime start;

  @Column(name = "conference_end")
  private LocalDateTime end;

  @Column(name = "venue", nullable = false)
  private String venue;

  @Column(name = "sales_start")
  private LocalDateTime salesStart;

  @Column(name = "sales_end")
  private LocalDateTime salesEnd;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private ConferenceStatusEnum status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organizer_id")
  private User organizer;

  @ManyToMany(mappedBy = "attendingConferences")
  private List<User> attendees = new ArrayList<>();

  @ManyToMany(mappedBy = "staffingConferences")
  private List<User> staff = new ArrayList<>();

  @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PassTier> passTiers = new ArrayList<>();

  @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Session> sessions = new ArrayList<>();

  @CreatedDate
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Conference that = (Conference) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name)
        && Objects.equals(start, that.start) && Objects.equals(end, that.end)
        && Objects.equals(venue, that.venue) && Objects.equals(salesStart, that.salesStart)
        && Objects.equals(salesEnd, that.salesEnd) && status == that.status
        && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, start, end, venue, salesStart, salesEnd, status, createdAt,
        updatedAt);
  }
}
