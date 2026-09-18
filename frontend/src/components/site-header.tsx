import { useAuth } from "react-oidc-context";
import { Link } from "react-router";
import { Ticket, LogOut, User as UserIcon } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  Avatar,
  AvatarFallback,
} from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useRoles } from "@/hooks/use-roles";

export default function SiteHeader() {
  const auth = useAuth();
  const { isOrganizer, isStaff } = useRoles();

  const name =
    (auth.user?.profile?.preferred_username as string | undefined) ??
    (auth.user?.profile?.name as string | undefined) ??
    "Account";
  const initial = name.charAt(0).toUpperCase();

  return (
    <header className="border-b bg-background/95 sticky top-0 z-40 backdrop-blur">
      <div className="mx-auto flex h-14 max-w-6xl items-center justify-between px-4">
        <Link to="/" className="flex items-center gap-2 font-semibold">
          <Ticket className="size-5" />
          Converge
        </Link>

        <nav className="flex items-center gap-2">
          <Button variant="ghost" size="sm" asChild>
            <Link to="/">Conferences</Link>
          </Button>

          {auth.isAuthenticated ? (
            <>
              <Button variant="ghost" size="sm" asChild>
                <Link to="/dashboard/badges">My Badges</Link>
              </Button>
              {isOrganizer && (
                <Button variant="ghost" size="sm" asChild>
                  <Link to="/dashboard/conferences">Organize</Link>
                </Button>
              )}
              {isStaff && (
                <Button variant="ghost" size="sm" asChild>
                  <Link to="/dashboard/check-in">Check-in</Link>
                </Button>
              )}
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Avatar className="cursor-pointer">
                    <AvatarFallback>{initial}</AvatarFallback>
                  </Avatar>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                  <DropdownMenuLabel className="flex items-center gap-2">
                    <UserIcon className="size-4" />
                    {name}
                  </DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem
                    variant="destructive"
                    onClick={() =>
                      void auth.signoutRedirect({
                        post_logout_redirect_uri: window.location.origin,
                      })
                    }
                  >
                    <LogOut className="size-4" />
                    Sign out
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            </>
          ) : (
            <Button
              size="sm"
              onClick={() => void auth.signinRedirect()}
            >
              Sign in
            </Button>
          )}
        </nav>
      </div>
    </header>
  );
}
