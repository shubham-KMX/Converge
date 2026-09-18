package com.example.conference.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "pass_tiers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassTier {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private Double price;

  @Column(name = "description")
  private String description;

  @Column(name = "total_available")
  private Integer totalAvailable;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "conference_id")
  private Conference conference;

  @OneToMany(mappedBy = "passTier", cascade = CascadeType.ALL)
  private List<Badge> badges = new ArrayList<>();

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
    PassTier passTier = (PassTier) o;
    return Objects.equals(id, passTier.id) && Objects.equals(name, passTier.name)
        && Objects.equals(price, passTier.price) && Objects.equals(description,
        passTier.description) && Objects.equals(totalAvailable, passTier.totalAvailable)
        && Objects.equals(createdAt, passTier.createdAt) && Objects.equals(updatedAt,
        passTier.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, description, totalAvailable, createdAt, updatedAt);
  }
}
