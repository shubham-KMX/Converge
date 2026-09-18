// Domain types mirroring the backend DTOs.

export type ConferenceStatus = "DRAFT" | "PUBLISHED" | "CANCELLED" | "COMPLETED";
export type BadgeStatus = "PURCHASED" | "CANCELLED";
export type CheckInStatus = "VALID" | "INVALID" | "EXPIRED";
export type CheckInMethod = "QR_SCAN" | "MANUAL";

export interface SpringBootPagination<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

// ---- Pass tiers ----
export interface CreatePassTierRequest {
  name: string;
  price: number;
  description?: string;
  totalAvailable?: number;
}

export interface UpdatePassTierRequest extends CreatePassTierRequest {
  id?: string;
}

// ---- Sessions ----
export interface CreateSessionRequest {
  title: string;
  description?: string;
  room?: string;
  start?: string;
  end?: string;
  speakerId?: string;
}

export interface UpdateSessionRequest extends CreateSessionRequest {
  id?: string;
}

export interface SessionSummary {
  id: string;
  title: string;
  description?: string;
  room?: string;
  start?: string;
  end?: string;
  speakerId?: string;
  speakerName?: string;
}

// ---- Speakers ----
export interface CreateSpeakerRequest {
  name: string;
  title?: string;
  company?: string;
  bio?: string;
}

export interface Speaker {
  id: string;
  name: string;
  title?: string;
  company?: string;
  bio?: string;
}

// ---- Conferences ----
export interface CreateConferenceRequest {
  name: string;
  start?: string;
  end?: string;
  venue: string;
  salesStart?: string;
  salesEnd?: string;
  status: ConferenceStatus;
  passTiers: CreatePassTierRequest[];
  sessions: CreateSessionRequest[];
}

export interface UpdateConferenceRequest {
  id: string;
  name: string;
  start?: string;
  end?: string;
  venue: string;
  salesStart?: string;
  salesEnd?: string;
  status: ConferenceStatus;
  passTiers: UpdatePassTierRequest[];
  sessions: UpdateSessionRequest[];
}

export interface PassTierSummary {
  id: string;
  name: string;
  price: number;
  description?: string;
  totalAvailable?: number;
}

export interface ConferenceListItem {
  id: string;
  name: string;
  start?: string;
  end?: string;
  venue: string;
  status: ConferenceStatus;
  passTiers?: PassTierSummary[];
}

export interface ConferenceDetails {
  id: string;
  name: string;
  start?: string;
  end?: string;
  venue: string;
  salesStart?: string;
  salesEnd?: string;
  status: ConferenceStatus;
  passTiers: PassTierSummary[];
  sessions: SessionSummary[];
}

export interface PublishedConferenceListItem {
  id: string;
  name: string;
  start?: string;
  end?: string;
  venue: string;
}

export interface PublishedConferenceDetails {
  id: string;
  name: string;
  start?: string;
  end?: string;
  venue: string;
  passTiers: PassTierSummary[];
  sessions: SessionSummary[];
}

// ---- Badges ----
export interface BadgeListItem {
  id: string;
  status: BadgeStatus;
  passTier: {
    id: string;
    name: string;
    price: number;
  };
}

export interface BadgeDetails {
  id: string;
  status: BadgeStatus;
  price: number;
  description?: string;
  conferenceName: string;
  venue: string;
  start?: string;
  end?: string;
}

// ---- Check-ins ----
export interface CheckInRequest {
  id: string;
  method: CheckInMethod;
}

export interface CheckInResponse {
  id: string;
  badgeId: string;
  status: CheckInStatus;
  checkInMethod: CheckInMethod;
}
