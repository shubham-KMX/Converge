import { Link } from "react-router";
import { CalendarCog, QrCode, ScanLine } from "lucide-react";

import PageShell from "@/components/page-shell";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useRoles } from "@/hooks/use-roles";

export default function DashboardPage() {
  const { isOrganizer, isStaff } = useRoles();

  return (
    <PageShell>
      <h1 className="mb-6 text-2xl font-bold tracking-tight">Dashboard</h1>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <Link to="/dashboard/badges">
          <Card className="h-full transition-shadow hover:shadow-md">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <QrCode className="size-5" />
                My Badges
              </CardTitle>
            </CardHeader>
            <CardContent className="text-muted-foreground text-sm">
              View your passes and QR badges.
            </CardContent>
          </Card>
        </Link>

        {isOrganizer && (
          <Link to="/dashboard/conferences">
            <Card className="h-full transition-shadow hover:shadow-md">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <CalendarCog className="size-5" />
                  Organize
                </CardTitle>
              </CardHeader>
              <CardContent className="text-muted-foreground text-sm">
                Create and manage your conferences.
              </CardContent>
            </Card>
          </Link>
        )}

        {isStaff && (
          <Link to="/dashboard/check-in">
            <Card className="h-full transition-shadow hover:shadow-md">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <ScanLine className="size-5" />
                  Check-in
                </CardTitle>
              </CardHeader>
              <CardContent className="text-muted-foreground text-sm">
                Scan attendee badges at the door.
              </CardContent>
            </Card>
          </Link>
        )}
      </div>
    </PageShell>
  );
}
