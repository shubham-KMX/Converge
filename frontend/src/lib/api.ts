import type {
  BadgeDetails,
  BadgeListItem,
  CheckInRequest,
  CheckInResponse,
  ConferenceDetails,
  ConferenceListItem,
  CreateConferenceRequest,
  CreateSpeakerRequest,
  PublishedConferenceDetails,
  PublishedConferenceListItem,
  SessionSummary,
  Speaker,
  SpringBootPagination,
  UpdateConferenceRequest,
} from "@/domain/domain";

const BASE = "/api/v1";

class ApiError extends Error {
  status: number;
  constructor(status: number, message: string) {
    super(message);
    this.status = status;
  }
}

async function request<T>(
  path: string,
  options: RequestInit = {},
  token?: string,
): Promise<T> {
  const headers = new Headers(options.headers);
  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }
  if (options.body && !headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }

  const res = await fetch(`${BASE}${path}`, { ...options, headers });

  if (!res.ok) {
    let message = res.statusText;
    try {
      const data = await res.json();
      if (data?.error) message = data.error;
    } catch {
      // no JSON body
    }
    throw new ApiError(res.status, message);
  }

  if (res.status === 204) {
    return undefined as T;
  }

  const contentType = res.headers.get("Content-Type") ?? "";
  if (contentType.includes("application/json")) {
    return (await res.json()) as T;
  }
  return (await res.text()) as unknown as T;
}

// ---- Public (no auth) ----
export function listPublishedConferences(query?: string) {
  const q = query ? `?q=${encodeURIComponent(query)}` : "";
  return request<SpringBootPagination<PublishedConferenceListItem>>(
    `/published-conferences${q}`,
  );
}

export function getPublishedConference(id: string) {
  return request<PublishedConferenceDetails>(`/published-conferences/${id}`);
}

export function listSessions(conferenceId: string) {
  return request<SessionSummary[]>(
    `/published-conferences/${conferenceId}/sessions`,
  );
}

// ---- Organizer ----
export function createConference(body: CreateConferenceRequest, token: string) {
  return request<ConferenceDetails>(
    `/conferences`,
    { method: "POST", body: JSON.stringify(body) },
    token,
  );
}

export function updateConference(
  id: string,
  body: UpdateConferenceRequest,
  token: string,
) {
  return request<ConferenceDetails>(
    `/conferences/${id}`,
    { method: "PUT", body: JSON.stringify(body) },
    token,
  );
}

export function listMyConferences(token: string, page = 0, size = 20) {
  return request<SpringBootPagination<ConferenceListItem>>(
    `/conferences?page=${page}&size=${size}`,
    {},
    token,
  );
}

export function getMyConference(id: string, token: string) {
  return request<ConferenceDetails>(`/conferences/${id}`, {}, token);
}

export function deleteConference(id: string, token: string) {
  return request<void>(`/conferences/${id}`, { method: "DELETE" }, token);
}

// ---- Speakers ----
export function listSpeakers(token: string) {
  return request<Speaker[]>(`/speakers`, {}, token);
}

export function createSpeaker(body: CreateSpeakerRequest, token: string) {
  return request<Speaker>(
    `/speakers`,
    { method: "POST", body: JSON.stringify(body) },
    token,
  );
}

// ---- Attendee: purchase + badges ----
export function purchasePass(
  conferenceId: string,
  passTierId: string,
  token: string,
) {
  return request<void>(
    `/conferences/${conferenceId}/pass-tiers/${passTierId}/badges`,
    { method: "POST" },
    token,
  );
}

export function listMyBadges(token: string, page = 0, size = 20) {
  return request<SpringBootPagination<BadgeListItem>>(
    `/badges?page=${page}&size=${size}`,
    {},
    token,
  );
}

export function getMyBadge(id: string, token: string) {
  return request<BadgeDetails>(`/badges/${id}`, {}, token);
}

// Returns an object URL for the badge QR PNG.
export async function getBadgeQrObjectUrl(
  badgeId: string,
  token: string,
): Promise<string> {
  const res = await fetch(`${BASE}/badges/${badgeId}/qr-codes`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) {
    throw new ApiError(res.status, "Failed to load QR code");
  }
  const blob = await res.blob();
  return URL.createObjectURL(blob);
}

// ---- Staff: check-in ----
export function checkIn(body: CheckInRequest, token: string) {
  return request<CheckInResponse>(
    `/check-ins`,
    { method: "POST", body: JSON.stringify(body) },
    token,
  );
}

export { ApiError };
