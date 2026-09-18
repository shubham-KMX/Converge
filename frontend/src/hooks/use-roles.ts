import { useAuth } from "react-oidc-context";

interface RealmAccess {
  roles?: string[];
}

/**
 * Reads Keycloak realm roles from the access token profile.
 * Roles come through as ROLE_ORGANIZER / ROLE_ATTENDEE / ROLE_STAFF.
 */
export function useRoles() {
  const auth = useAuth();

  const profile = auth.user?.profile as
    | { realm_access?: RealmAccess }
    | undefined;
  const roles = profile?.realm_access?.roles ?? [];

  return {
    roles,
    isOrganizer: roles.includes("ROLE_ORGANIZER"),
    isAttendee: roles.includes("ROLE_ATTENDEE"),
    isStaff: roles.includes("ROLE_STAFF"),
  };
}
