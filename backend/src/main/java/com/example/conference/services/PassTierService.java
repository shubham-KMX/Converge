package com.example.conference.services;

import com.example.conference.domain.entities.Badge;
import java.util.UUID;

public interface PassTierService {

  Badge purchasePass(UUID attendeeId, UUID passTierId);
}
