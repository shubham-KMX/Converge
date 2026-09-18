import { useEffect, useState } from "react";
import { Link } from "react-router";
import { Plus } from "lucide-react";

import PageShell from "@/components/page-shell";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { listMyConferences } from "@/lib/api";
import { useToken } from "@/hooks/use-token";
import type { ConferenceListItem } from "@/domain/domain";

export default function MyConferencesPage() {
  const getToken = useToken();
  const [conferences, setConferences] = useState<ConferenceListItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    listMyConferences(getToken())
      .then((page) => setConferences(page.content))
      .catch(() => setConferences([]))
      .finally(() => setLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <PageShell>
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold tracking-tight">My Conferences</h1>
        <Button asChild>
          <Link to="/dashboard/conferences/create">
            <Plus className="size-4" />
            New conference
          </Link>
        </Button>
      </div>

      {loading ? (
        <p className="text-muted-foreground">Loading…</p>
      ) : conferences.length === 0 ? (
        <p className="text-muted-foreground">
          You haven't created any conferences yet.
        </p>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {conferences.map((c) => (
            <Link key={c.id} to={`/dashboard/conferences/update/${c.id}`}>
              <Card className="h-full transition-shadow hover:shadow-md">
                <CardHeader>
                  <CardTitle className="flex items-center justify-between gap-2">
                    <span className="truncate">{c.name}</span>
                    <Badge
                      variant={
                        c.status === "PUBLISHED" ? "default" : "secondary"
                      }
                    >
                      {c.status}
                    </Badge>
                  </CardTitle>
                </CardHeader>
                <CardContent className="text-muted-foreground text-sm">
                  {c.venue}
                  {c.passTiers && ` · ${c.passTiers.length} pass tier(s)`}
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>
      )}
    </PageShell>
  );
}
