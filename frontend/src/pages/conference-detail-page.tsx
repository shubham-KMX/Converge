import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { useAuth } from "react-oidc-context";
import { format } from "date-fns";
import { CalendarDays, Clock, MapPin, Mic } from "lucide-react";
import { toast } from "sonner";

import PageShell from "@/components/page-shell";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { getPublishedConference, purchasePass } from "@/lib/api";
import { useToken } from "@/hooks/use-token";
import type { PublishedConferenceDetails } from "@/domain/domain";

export default function ConferenceDetailPage() {
  const { id } = useParams();
  const auth = useAuth();
  const getToken = useToken();
  const navigate = useNavigate();
  const [conference, setConference] = useState<PublishedConferenceDetails>();
  const [loading, setLoading] = useState(true);
  const [purchasing, setPurchasing] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    getPublishedConference(id)
      .then(setConference)
      .catch(() => setConference(undefined))
      .finally(() => setLoading(false));
  }, [id]);

  async function handlePurchase(passTierId: string) {
    if (!auth.isAuthenticated) {
      void auth.signinRedirect();
      return;
    }
    if (!id) return;
    setPurchasing(passTierId);
    try {
      await purchasePass(id, passTierId, getToken());
      toast.success("Pass purchased! Your badge is ready.");
      void navigate("/dashboard/badges");
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Purchase failed");
    } finally {
      setPurchasing(null);
    }
  }

  if (loading) {
    return (
      <PageShell>
        <p className="text-muted-foreground">Loading…</p>
      </PageShell>
    );
  }

  if (!conference) {
    return (
      <PageShell>
        <p className="text-muted-foreground">Conference not found.</p>
      </PageShell>
    );
  }

  return (
    <PageShell>
      <div className="mb-8">
        <h1 className="text-3xl font-bold tracking-tight">
          {conference.name}
        </h1>
        <div className="text-muted-foreground mt-3 flex flex-wrap gap-4 text-sm">
          <span className="flex items-center gap-2">
            <MapPin className="size-4" />
            {conference.venue}
          </span>
          {conference.start && (
            <span className="flex items-center gap-2">
              <CalendarDays className="size-4" />
              {format(new Date(conference.start), "d MMM yyyy")}
              {conference.end &&
                ` — ${format(new Date(conference.end), "d MMM yyyy")}`}
            </span>
          )}
        </div>
      </div>

      <div className="grid gap-8 lg:grid-cols-2">
        <section>
          <h2 className="mb-4 text-xl font-semibold">Passes</h2>
          <div className="space-y-4">
            {conference.passTiers.map((pt) => (
              <Card key={pt.id}>
                <CardHeader>
                  <CardTitle className="flex items-center justify-between">
                    {pt.name}
                    <span className="text-primary">${pt.price.toFixed(2)}</span>
                  </CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  {pt.description && (
                    <p className="text-muted-foreground text-sm">
                      {pt.description}
                    </p>
                  )}
                  <Button
                    className="w-full"
                    disabled={purchasing === pt.id}
                    onClick={() => handlePurchase(pt.id)}
                  >
                    {purchasing === pt.id ? "Purchasing…" : "Buy pass"}
                  </Button>
                </CardContent>
              </Card>
            ))}
          </div>
        </section>

        <section>
          <h2 className="mb-4 text-xl font-semibold">Sessions</h2>
          {conference.sessions.length === 0 ? (
            <p className="text-muted-foreground text-sm">
              No sessions announced yet.
            </p>
          ) : (
            <div className="space-y-4">
              {conference.sessions.map((s) => (
                <Card key={s.id}>
                  <CardHeader>
                    <CardTitle className="text-base">{s.title}</CardTitle>
                  </CardHeader>
                  <CardContent className="text-muted-foreground space-y-2 text-sm">
                    {s.description && <p>{s.description}</p>}
                    <div className="flex flex-wrap gap-3">
                      {s.speakerName && (
                        <span className="flex items-center gap-1">
                          <Mic className="size-3.5" />
                          {s.speakerName}
                        </span>
                      )}
                      {s.room && <Badge variant="secondary">{s.room}</Badge>}
                      {s.start && (
                        <span className="flex items-center gap-1">
                          <Clock className="size-3.5" />
                          {format(new Date(s.start), "d MMM, HH:mm")}
                        </span>
                      )}
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </section>
      </div>
    </PageShell>
  );
}
