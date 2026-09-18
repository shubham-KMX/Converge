import { useEffect } from "react";
import { useAuth } from "react-oidc-context";
import { useNavigate } from "react-router";

export default function CallbackPage() {
  const auth = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!auth.isLoading) {
      if (auth.isAuthenticated) {
        void navigate("/dashboard", { replace: true });
      } else if (auth.error) {
        void navigate("/", { replace: true });
      }
    }
  }, [auth.isLoading, auth.isAuthenticated, auth.error, navigate]);

  return (
    <div className="flex min-h-screen items-center justify-center text-muted-foreground">
      Signing you in…
    </div>
  );
}
