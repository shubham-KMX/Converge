import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { Plus, Trash2 } from "lucide-react";
import { toast } from "sonner";

import PageShell from "@/components/page-shell";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  createConference,
  getMyConference,
  listSpeakers,
  updateConference,
} from "@/lib/api";
import { useToken } from "@/hooks/use-token";
import type {
  ConferenceStatus,
  Speaker,
  UpdatePassTierRequest,
  UpdateSessionRequest,
} from "@/domain/domain";

const STATUSES: ConferenceStatus[] = [
  "DRAFT",
  "PUBLISHED",
  "CANCELLED",
  "COMPLETED",
];

const NO_SPEAKER = "__none__";

export default function ManageConferencePage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const getToken = useToken();
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [venue, setVenue] = useState("");
  const [start, setStart] = useState("");
  const [end, setEnd] = useState("");
  const [status, setStatus] = useState<ConferenceStatus>("DRAFT");
  const [passTiers, setPassTiers] = useState<UpdatePassTierRequest[]>([
    { name: "", price: 0, description: "", totalAvailable: 100 },
  ]);
  const [sessions, setSessions] = useState<UpdateSessionRequest[]>([]);
  const [speakers, setSpeakers] = useState<Speaker[]>([]);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    listSpeakers(getToken())
      .then(setSpeakers)
      .catch(() => setSpeakers([]));
    if (isEdit && id) {
      getMyConference(id, getToken())
        .then((c) => {
          setName(c.name);
          setVenue(c.venue);
          setStart(c.start?.slice(0, 16) ?? "");
          setEnd(c.end?.slice(0, 16) ?? "");
          setStatus(c.status);
          setPassTiers(
            c.passTiers.map((pt) => ({
              id: pt.id,
              name: pt.name,
              price: pt.price,
              description: pt.description ?? "",
              totalAvailable: pt.totalAvailable ?? 0,
            })),
          );
          setSessions(
            c.sessions.map((s) => ({
              id: s.id,
              title: s.title,
              description: s.description ?? "",
              room: s.room ?? "",
              start: s.start?.slice(0, 16) ?? "",
              end: s.end?.slice(0, 16) ?? "",
              speakerId: s.speakerId,
            })),
          );
        })
        .catch(() => toast.error("Failed to load conference"));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  function updatePassTier(i: number, patch: Partial<UpdatePassTierRequest>) {
    setPassTiers((prev) =>
      prev.map((pt, idx) => (idx === i ? { ...pt, ...patch } : pt)),
    );
  }

  function updateSession(i: number, patch: Partial<UpdateSessionRequest>) {
    setSessions((prev) =>
      prev.map((s, idx) => (idx === i ? { ...s, ...patch } : s)),
    );
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setSaving(true);
    const payload = {
      name,
      venue,
      start: start || undefined,
      end: end || undefined,
      status,
      passTiers: passTiers.map((pt) => ({
        ...pt,
        price: Number(pt.price),
        totalAvailable: pt.totalAvailable ? Number(pt.totalAvailable) : undefined,
      })),
      sessions: sessions.map((s) => ({
        ...s,
        start: s.start || undefined,
        end: s.end || undefined,
        speakerId: s.speakerId || undefined,
      })),
    };
    try {
      if (isEdit && id) {
        await updateConference(id, { id, ...payload }, getToken());
        toast.success("Conference updated");
      } else {
        await createConference(payload, getToken());
        toast.success("Conference created");
      }
      void navigate("/dashboard/conferences");
    } catch (err) {
      toast.error(err instanceof Error ? err.message : "Save failed");
    } finally {
      setSaving(false);
    }
  }

  return (
    <PageShell>
      <h1 className="mb-6 text-2xl font-bold tracking-tight">
        {isEdit ? "Edit conference" : "New conference"}
      </h1>

      <form onSubmit={handleSubmit} className="space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Details</CardTitle>
          </CardHeader>
          <CardContent className="grid gap-4 sm:grid-cols-2">
            <div className="space-y-2 sm:col-span-2">
              <Label htmlFor="name">Name</Label>
              <Input
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </div>
            <div className="space-y-2 sm:col-span-2">
              <Label htmlFor="venue">Venue</Label>
              <Input
                id="venue"
                value={venue}
                onChange={(e) => setVenue(e.target.value)}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="start">Start</Label>
              <Input
                id="start"
                type="datetime-local"
                value={start}
                onChange={(e) => setStart(e.target.value)}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="end">End</Label>
              <Input
                id="end"
                type="datetime-local"
                value={end}
                onChange={(e) => setEnd(e.target.value)}
              />
            </div>
            <div className="space-y-2">
              <Label>Status</Label>
              <Select
                value={status}
                onValueChange={(v) => setStatus(v as ConferenceStatus)}
              >
                <SelectTrigger className="w-full">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {STATUSES.map((s) => (
                    <SelectItem key={s} value={s}>
                      {s}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center justify-between">
              Pass tiers
              <Button
                type="button"
                variant="outline"
                size="sm"
                onClick={() =>
                  setPassTiers((prev) => [
                    ...prev,
                    { name: "", price: 0, description: "", totalAvailable: 100 },
                  ])
                }
              >
                <Plus className="size-4" />
                Add
              </Button>
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {passTiers.map((pt, i) => (
              <div
                key={i}
                className="grid gap-3 rounded-lg border p-4 sm:grid-cols-[1fr_auto_auto_auto]"
              >
                <Input
                  placeholder="Name"
                  value={pt.name}
                  onChange={(e) => updatePassTier(i, { name: e.target.value })}
                  required
                />
                <Input
                  type="number"
                  step="0.01"
                  placeholder="Price"
                  className="w-28"
                  value={pt.price}
                  onChange={(e) =>
                    updatePassTier(i, { price: Number(e.target.value) })
                  }
                  required
                />
                <Input
                  type="number"
                  placeholder="Qty"
                  className="w-24"
                  value={pt.totalAvailable ?? ""}
                  onChange={(e) =>
                    updatePassTier(i, {
                      totalAvailable: Number(e.target.value),
                    })
                  }
                />
                <Button
                  type="button"
                  variant="ghost"
                  size="icon"
                  onClick={() =>
                    setPassTiers((prev) => prev.filter((_, idx) => idx !== i))
                  }
                >
                  <Trash2 className="size-4" />
                </Button>
              </div>
            ))}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center justify-between">
              Sessions
              <Button
                type="button"
                variant="outline"
                size="sm"
                onClick={() =>
                  setSessions((prev) => [
                    ...prev,
                    { title: "", room: "", description: "" },
                  ])
                }
              >
                <Plus className="size-4" />
                Add
              </Button>
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {sessions.length === 0 && (
              <p className="text-muted-foreground text-sm">
                No sessions yet.
              </p>
            )}
            {sessions.map((s, i) => (
              <div key={i} className="grid gap-3 rounded-lg border p-4">
                <div className="flex gap-3">
                  <Input
                    placeholder="Title"
                    value={s.title}
                    onChange={(e) => updateSession(i, { title: e.target.value })}
                    required
                  />
                  <Button
                    type="button"
                    variant="ghost"
                    size="icon"
                    onClick={() =>
                      setSessions((prev) => prev.filter((_, idx) => idx !== i))
                    }
                  >
                    <Trash2 className="size-4" />
                  </Button>
                </div>
                <div className="grid gap-3 sm:grid-cols-3">
                  <Input
                    placeholder="Room"
                    value={s.room ?? ""}
                    onChange={(e) => updateSession(i, { room: e.target.value })}
                  />
                  <Input
                    type="datetime-local"
                    value={s.start ?? ""}
                    onChange={(e) => updateSession(i, { start: e.target.value })}
                  />
                  <Select
                    value={s.speakerId ?? NO_SPEAKER}
                    onValueChange={(v) =>
                      updateSession(i, {
                        speakerId: v === NO_SPEAKER ? undefined : v,
                      })
                    }
                  >
                    <SelectTrigger className="w-full">
                      <SelectValue placeholder="Speaker" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value={NO_SPEAKER}>No speaker</SelectItem>
                      {speakers.map((sp) => (
                        <SelectItem key={sp.id} value={sp.id}>
                          {sp.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>
            ))}
          </CardContent>
        </Card>

        <div className="flex justify-end gap-2">
          <Button
            type="button"
            variant="outline"
            onClick={() => void navigate("/dashboard/conferences")}
          >
            Cancel
          </Button>
          <Button type="submit" disabled={saving}>
            {saving ? "Saving…" : isEdit ? "Save changes" : "Create"}
          </Button>
        </div>
      </form>
    </PageShell>
  );
}
