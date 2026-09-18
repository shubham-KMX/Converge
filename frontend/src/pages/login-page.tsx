import { useEffect } from "react";
import { useAuth } from "react-oidc-context";

export default function LoginPage() {
  const auth = useAuth();

  useEffect(() => {
    if (!auth.isLoading && !auth.isAuthenticated) {
      void auth.signinRedirect();
    }
  }, [auth.isLoading, auth.isAuthenticated, auth]);

  return (
    <div className="flex min-h-screen items-center justify-center text-muted-foreground">
      Redirecting to sign in…
    </div>
  );
}
