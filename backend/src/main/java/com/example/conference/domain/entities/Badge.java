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
@Table(name = "badges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private BadgeStatusEnum status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pass_tier_id")
  private PassTier passTier;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attendee_id")
  private User attendee;

  @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL)
  private List<CheckIn> checkIns = new ArrayList<>();

  @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL)
  private List<QrCode> qrCodes = new ArrayList<>();

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
    Badge badge = (Badge) o;
    return Objects.equals(id, badge.id) && status == badge.status
        && Objects.equals(createdAt, badge.createdAt) && Objects.equals(updatedAt, badge.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, status, createdAt, updatedAt);
  }
}
