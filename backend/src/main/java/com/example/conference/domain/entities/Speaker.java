package com.example.conference.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "speakers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Speaker {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "title")
  private String title;

  @Column(name = "company")
  private String company;

  @Column(name = "bio", columnDefinition = "TEXT")
  private String bio;

  @OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL)
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
    Speaker speaker = (Speaker) o;
    return Objects.equals(id, speaker.id) && Objects.equals(name, speaker.name)
        && Objects.equals(title, speaker.title) && Objects.equals(company, speaker.company)
        && Objects.equals(bio, speaker.bio) && Objects.equals(createdAt, speaker.createdAt)
        && Objects.equals(updatedAt, speaker.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, title, company, bio, createdAt, updatedAt);
  }
}
