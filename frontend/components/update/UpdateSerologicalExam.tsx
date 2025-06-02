"use client";

import { Select, SelectItem, Textarea, Button } from "@heroui/react";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

import {
  getSerologicalExamByDonationId,
  updateSerologicalExam,
} from "@/services/api";
import showSuccessToast from "@/services/toast/showSuccessToast";
import showFailToast from "@/services/toast/showFailToast";

export default function UpdateSerologicalExam() {
  const { donationId } = useParams();
  const router = useRouter();

  const [exam, setExam] = useState<any>(null);
  const [observations, setObservations] = useState("");
  const [fields, setFields] = useState({
    hepatitisB: "",
    hepatitisC: "",
    chagasDisease: "",
    syphilis: "",
    aids: "",
    htlv1_2: "",
  });

  useEffect(() => {
    if (donationId) {
      getSerologicalExamByDonationId(donationId as string).then((data) => {
        setExam(data);
        setObservations(data.observations ?? "");
        setFields({
          hepatitisB: data.hepatitisB ?? "",
          hepatitisC: data.hepatitisC ?? "",
          chagasDisease: data.chagasDisease ?? "",
          syphilis: data.syphilis ?? "",
          aids: data.aids ?? "",
          htlv1_2: data.htlv1_2 ?? "",
        });
      });
    }
  }, [donationId]);

  const handleChange = (key: string, value: string) => {
    setFields((prev) => ({ ...prev, [key]: value }));
  };

  const handleSubmit = async (approve: boolean) => {
    try {
      await updateSerologicalExam(exam, { ...fields }, approve);
      if (approve) {
        showSuccessToast("Exam approved!");
      } else {
        showSuccessToast("Exam rejected!");
      }
      router.push(`/donation?tab=update&donationId=${donationId}`);
    } catch (error) {
      showFailToast("Error upadating exam");
    }
  };

  if (!exam) return <p className="mt-10 text-center">Loading exam...</p>;

  return (
    <div className="max-w-lg mx-auto mt-10 space-y-6">
      <h1 className="text-2xl font-semibold">
        Update Serological Screening Exam
      </h1>

      {Object.entries(fields).map(([key, value]) => (
        <Select
          key={key}
          isRequired
          id={`${key}-select`}
          label={key.replace(/([A-Z])/g, " $1").toUpperCase()}
          selectedKeys={[value]}
          onSelectionChange={(keys) =>
            handleChange(key, Array.from(keys)[0] as string)
          }
        >
          <SelectItem key="NEGATIVE">Negative</SelectItem>
          <SelectItem key="POSITIVE">Positive</SelectItem>
        </Select>
      ))}

      <Textarea
        label="Observations"
        placeholder="Relevant clinical observations..."
        value={observations}
        onChange={(e) => setObservations(e.target.value)}
      />

      <div className="flex justify-between gap-4">
        <Button
          color="danger"
          id="reject-button"
          onPress={() => handleSubmit(false)}
        >
          Reject
        </Button>
        <Button
          color="primary"
          id="approve-button"
          onPress={() => handleSubmit(true)}
        >
          Approve
        </Button>
      </div>
    </div>
  );
}
