import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { AuthProvider } from "react-oidc-context";
import { createBrowserRouter, RouterProvider } from "react-router";
import { WebStorageStateStore } from "oidc-client-ts";

import "./index.css";
import { Toaster } from "@/components/ui/sonner";
import ProtectedRoute from "@/components/protected-route";
import LandingPage from "@/pages/landing-page";
import ConferenceDetailPage from "@/pages/conference-detail-page";
import LoginPage from "@/pages/login-page";
import CallbackPage from "@/pages/callback-page";
import MyBadgesPage from "@/pages/my-badges-page";
import ViewBadgePage from "@/pages/view-badge-page";
import MyConferencesPage from "@/pages/my-conferences-page";
import ManageConferencePage from "@/pages/manage-conference-page";
import CheckInPage from "@/pages/check-in-page";

const router = createBrowserRouter([
  { path: "/", Component: LandingPage },
  { path: "/login", Component: LoginPage },
  { path: "/callback", Component: CallbackPage },
  { path: "/conferences/:id", Component: ConferenceDetailPage },
  {
    path: "/dashboard/badges",
    element: (
      <ProtectedRoute>
        <MyBadgesPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/badges/:id",
    element: (
      <ProtectedRoute>
        <ViewBadgePage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/conferences",
    element: (
      <ProtectedRoute>
        <MyConferencesPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/conferences/create",
    element: (
      <ProtectedRoute>
        <ManageConferencePage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/conferences/update/:id",
    element: (
      <ProtectedRoute>
        <ManageConferencePage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/check-in",
    element: (
      <ProtectedRoute>
        <CheckInPage />
      </ProtectedRoute>
    ),
  },
]);

const oidcConfig = {
  authority: "http://localhost:9090/realms/conference-platform",
  client_id: "conference-platform-app",
  redirect_uri: "http://localhost:5173/callback",
  post_logout_redirect_uri: "http://localhost:5173",
  userStore: new WebStorageStateStore({ store: window.localStorage }),
  onSigninCallback: () => {
    window.history.replaceState({}, document.title, window.location.pathname);
  },
};

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AuthProvider {...oidcConfig}>
      <RouterProvider router={router} />
      <Toaster richColors />
    </AuthProvider>
  </StrictMode>,
);
