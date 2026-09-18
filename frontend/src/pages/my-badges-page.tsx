import { useEffect, useState } from "react";
import { Link } from "react-router";
import { QrCode } from "lucide-react";

import PageShell from "@/components/page-shell";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { listMyBadges } from "@/lib/api";
import { useToken } from "@/hooks/use-token";
import type { BadgeListItem } from "@/domain/domain";

export default function MyBadgesPage() {
  const getToken = useToken();
  const [badges, setBadges] = useState<BadgeListItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    listMyBadges(getToken())
      .then((page) => setBadges(page.content))
      .catch(() => setBadges([]))
      .finally(() => setLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <PageShell>
      <h1 className="mb-6 text-2xl font-bold tracking-tight">My Badges</h1>
      {loading ? (
        <p className="text-muted-foreground">Loading…</p>
      ) : badges.length === 0 ? (
        <p className="text-muted-foreground">
          You have no badges yet. Browse conferences to buy a pass.
        </p>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {badges.map((b) => (
            <Card key={b.id}>
              <CardHeader>
                <CardTitle className="flex items-center justify-between">
                  {b.passTier.name}
                  <Badge
                    variant={b.status === "PURCHASED" ? "default" : "secondary"}
                  >
                    {b.status}
                  </Badge>
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <p className="text-muted-foreground text-sm">
                  ${b.passTier.price.toFixed(2)}
                </p>
                <Button variant="outline" className="w-full" asChild>
                  <Link to={`/dashboard/badges/${b.id}`}>
                    <QrCode className="size-4" />
                    View badge
                  </Link>
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </PageShell>
  );
}
