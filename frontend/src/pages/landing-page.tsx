import { useEffect, useState } from "react";
import { Link } from "react-router";
import { format } from "date-fns";
import { CalendarDays, MapPin, Search } from "lucide-react";

import PageShell from "@/components/page-shell";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { listPublishedConferences } from "@/lib/api";
import type { PublishedConferenceListItem } from "@/domain/domain";

function formatRange(start?: string, end?: string): string {
  if (!start) return "Dates TBA";
  const s = format(new Date(start), "d MMM yyyy");
  if (!end) return s;
  return `${s} — ${format(new Date(end), "d MMM yyyy")}`;
}

export default function LandingPage() {
  const [conferences, setConferences] = useState<
    PublishedConferenceListItem[]
  >([]);
  const [query, setQuery] = useState("");
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    listPublishedConferences(search || undefined)
      .then((page) => setConferences(page.content))
      .catch(() => setConferences([]))
      .finally(() => setLoading(false));
  }, [search]);

  return (
    <PageShell>
      <section className="mb-10 text-center">
        <h1 className="text-4xl font-bold tracking-tight">
          Find your next conference
        </h1>
        <p className="text-muted-foreground mt-3">
          Browse published conferences, grab a pass, and get your badge.
        </p>
        <form
          className="mx-auto mt-6 flex max-w-md gap-2"
          onSubmit={(e) => {
            e.preventDefault();
            setSearch(query.trim());
          }}
        >
          <Input
            placeholder="Search by name or venue…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <Button type="submit">
            <Search className="size-4" />
            Search
          </Button>
        </form>
      </section>

      {loading ? (
        <p className="text-muted-foreground text-center">Loading…</p>
      ) : conferences.length === 0 ? (
        <p className="text-muted-foreground text-center">
          No conferences found.
        </p>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {conferences.map((c) => (
            <Link key={c.id} to={`/conferences/${c.id}`}>
              <Card className="h-full transition-shadow hover:shadow-md">
                <CardHeader>
                  <CardTitle>{c.name}</CardTitle>
                </CardHeader>
                <CardContent className="text-muted-foreground space-y-2 text-sm">
                  <div className="flex items-center gap-2">
                    <CalendarDays className="size-4" />
                    {formatRange(c.start, c.end)}
                  </div>
                  <div className="flex items-center gap-2">
                    <MapPin className="size-4" />
                    {c.venue}
                  </div>
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>
      )}
    </PageShell>
  );
}
