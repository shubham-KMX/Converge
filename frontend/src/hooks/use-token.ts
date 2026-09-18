import { useAuth } from "react-oidc-context";

/** Returns the current access token, or throws if not authenticated. */
export function useToken(): () => string {
  const auth = useAuth();
  return () => {
    const token = auth.user?.access_token;
    if (!token) {
      throw new Error("Not authenticated");
    }
    return token;
  };
}
