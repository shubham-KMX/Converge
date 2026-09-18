import { useState } from "react";
import { Scanner, type IDetectedBarcode } from "@yudiel/react-qr-scanner";
import { CheckCircle2, XCircle } from "lucide-react";
import { toast } from "sonner";

import PageShell from "@/components/page-shell";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { checkIn } from "@/lib/api";
import { useToken } from "@/hooks/use-token";
import type { CheckInResponse } from "@/domain/domain";

export default function CheckInPage() {
  const getToken = useToken();
  const [scanning, setScanning] = useState(false);
  const [manualId, setManualId] = useState("");
  const [result, setResult] = useState<CheckInResponse | null>(null);
  const [busy, setBusy] = useState(false);

  async function submit(id: string, method: "QR_SCAN" | "MANUAL") {
    if (busy) return;
    setBusy(true);
    try {
      const res = await checkIn({ id, method }, getToken());
      setResult(res);
      if (res.status === "VALID") {
        toast.success("Checked in");
      } else {
        toast.error(`Check-in ${res.status.toLowerCase()}`);
      }
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Check-in failed");
    } finally {
      setBusy(false);
    }
  }

  function handleScan(codes: IDetectedBarcode[]) {
    const value = codes[0]?.rawValue;
    if (value) {
      setScanning(false);
      void submit(value, "QR_SCAN");
    }
  }

  return (
    <PageShell>
      <div className="mx-auto max-w-md space-y-6">
        <h1 className="text-2xl font-bold tracking-tight">Badge check-in</h1>

        <Card>
          <CardHeader>
            <CardTitle>Scan QR</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {scanning ? (
              <>
                <div className="overflow-hidden rounded-lg border">
                  <Scanner onScan={handleScan} />
                </div>
                <Button
                  variant="outline"
                  className="w-full"
                  onClick={() => setScanning(false)}
                >
                  Stop scanning
                </Button>
              </>
            ) : (
              <Button className="w-full" onClick={() => setScanning(true)}>
                Start camera
              </Button>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Manual entry</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="badgeId">Badge ID</Label>
              <Input
                id="badgeId"
                placeholder="Badge UUID"
                value={manualId}
                onChange={(e) => setManualId(e.target.value)}
              />
            </div>
            <Button
              className="w-full"
              disabled={!manualId || busy}
              onClick={() => submit(manualId.trim(), "MANUAL")}
            >
              Check in manually
            </Button>
          </CardContent>
        </Card>

        {result && (
          <Card
            className={
              result.status === "VALID"
                ? "border-green-500"
                : "border-destructive"
            }
          >
            <CardContent className="flex items-center gap-3 pt-6">
              {result.status === "VALID" ? (
                <CheckCircle2 className="size-8 text-green-600" />
              ) : (
                <XCircle className="text-destructive size-8" />
              )}
              <div>
                <p className="font-semibold">{result.status}</p>
                <p className="text-muted-foreground text-sm">
                  {result.status === "VALID"
                    ? "Attendee checked in"
                    : "Already checked in / invalid"}
                </p>
              </div>
            </CardContent>
          </Card>
        )}
      </div>
    </PageShell>
  );
}
