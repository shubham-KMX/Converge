# Conference Badge App

An event ticketing platform reimagined for **conferences**. Organizers create conferences, define
pass tiers and sessions, attendees buy passes and receive a QR badge, and staff scan badges to check
attendees in at the door.

It is built from scratch as a faithful re-implementation of a layered Spring Boot architecture
(controllers → services → repositories, DTOs and mappers at the API boundary, Keycloak for auth,
ZXing for QR codes), repurposed for the conference domain with sessions and speakers as first-class
features.

## Domain

| Concept    | Meaning                                                             |
| ---------- | ------------------------------------------------------------------- |
| Conference | A multi-day event with a venue and a sales window                   |
| Pass Tier  | A purchasable pass (e.g. General Admission, Workshop, VIP/Speaker)  |
| Session    | A talk/workshop within a conference, with a speaker, room, and time |
| Speaker    | The person presenting a session                                     |
| Badge      | An attendee's pass instance, carrying a QR code, scanned at check-in |

### Roles

- **Organizer** — creates conferences, pass tiers, and sessions
- **Attendee** — buys passes and receives QR badges
- **Staff** — scans badges to check attendees in

## Stack

- **Backend**: Spring Boot 3.4.4, Java 21, Spring Data JPA, Spring Security + OAuth2 resource server, MapStruct, Lombok, ZXing
- **Frontend**: React + Vite + TypeScript
- **Infra**: PostgreSQL, Keycloak, Adminer via Docker Compose

## Status

Work in progress. Being built backend-first, endpoint by endpoint.
