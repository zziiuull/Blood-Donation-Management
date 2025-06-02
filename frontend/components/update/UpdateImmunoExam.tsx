"use client";

import { Select, SelectItem, Button, Textarea } from "@heroui/react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { useRouter } from "next/navigation";

import { getImmunoExamByDonationId, updateImmunoExam } from "@/services/api";
import showSuccessToast from "@/services/toast/showSuccessToast";
import showFailToast from "@/services/toast/showFailToast";

export default function UpdateImmunoExam() {
  const { donationId } = useParams();
  const router = useRouter();

  const [exam, setExam] = useState<any>(null);
  const [bloodType, setBloodType] = useState("");
  const [irregularAntibodies, setIrregularAntibodies] = useState("");
  const [observations, setObservations] = useState("");

  useEffect(() => {
    if (donationId) {
      getImmunoExamByDonationId(donationId as string).then((data) => {
        setExam(data);
        setBloodType(data.bloodType ?? "");
        setIrregularAntibodies(data.irregularAntibodies ?? "");
        setObservations(data.observations ?? "");
      });
    }
  }, [donationId]);

  const handleSubmit = async (approve: boolean) => {
    try {
      await updateImmunoExam(
        exam,
        {
          bloodType,
          irregularAntibodies,
        },
        approve,
      );

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
        Update Immunohematological Exam
      </h1>

      <Select
        isRequired
        id="blood-type-select"
        label="Blood type"
        selectedKeys={[bloodType]}
        onSelectionChange={(keys) =>
          setBloodType(Array.from(keys)[0] as string)
        }
      >
        {[
          "A_POS",
          "A_NEG",
          "B_POS",
          "B_NEG",
          "AB_POS",
          "AB_NEG",
          "O_POS",
          "O_NEG",
        ].map((type) => (
          <SelectItem key={type}>{type.replace("_", " ")}</SelectItem>
        ))}
      </Select>

      <Select
        isRequired
        id="irregular-antibodies-select"
        label="Irregular antibodies"
        selectedKeys={[irregularAntibodies]}
        onSelectionChange={(keys) =>
          setIrregularAntibodies(Array.from(keys)[0] as string)
        }
      >
        <SelectItem key="NEGATIVE">Negative</SelectItem>
        <SelectItem key="POSITIVE">Positive</SelectItem>
      </Select>

      <Textarea
        id="observations-textarea"
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
