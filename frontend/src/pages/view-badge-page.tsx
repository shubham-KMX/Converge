import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { format } from "date-fns";
import { CalendarDays, MapPin } from "lucide-react";

import PageShell from "@/components/page-shell";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { getBadgeQrObjectUrl, getMyBadge } from "@/lib/api";
import { useToken } from "@/hooks/use-token";
import type { BadgeDetails } from "@/domain/domain";

export default function ViewBadgePage() {
  const { id } = useParams();
  const getToken = useToken();
  const [badge, setBadge] = useState<BadgeDetails>();
  const [qrUrl, setQrUrl] = useState<string>();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;
    const token = getToken();
    let revoke: string | undefined;
    Promise.all([getMyBadge(id, token), getBadgeQrObjectUrl(id, token)])
      .then(([b, url]) => {
        setBadge(b);
        setQrUrl(url);
        revoke = url;
      })
      .catch(() => setBadge(undefined))
      .finally(() => setLoading(false));
    return () => {
      if (revoke) URL.revokeObjectURL(revoke);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  if (loading) {
    return (
      <PageShell>
        <p className="text-muted-foreground">Loading…</p>
      </PageShell>
    );
  }

  if (!badge) {
    return (
      <PageShell>
        <p className="text-muted-foreground">Badge not found.</p>
      </PageShell>
    );
  }

  return (
    <PageShell>
      <div className="mx-auto max-w-md">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center justify-between">
              {badge.conferenceName}
              <Badge>{badge.status}</Badge>
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            {qrUrl && (
              <div className="flex justify-center">
                <img
                  src={qrUrl}
                  alt="Badge QR code"
                  className="size-64 rounded-lg border"
                />
              </div>
            )}
            <div className="text-muted-foreground space-y-2 text-sm">
              <div className="flex items-center gap-2">
                <MapPin className="size-4" />
                {badge.venue}
              </div>
              {badge.start && (
                <div className="flex items-center gap-2">
                  <CalendarDays className="size-4" />
                  {format(new Date(badge.start), "d MMM yyyy")}
                  {badge.end &&
                    ` — ${format(new Date(badge.end), "d MMM yyyy")}`}
                </div>
              )}
              <p className="pt-2">
                {badge.description ?? ""} · ${badge.price.toFixed(2)}
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    </PageShell>
  );
}
