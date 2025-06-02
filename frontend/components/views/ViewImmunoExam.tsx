"use client";

import { Card, CardBody, CardHeader, Divider } from "@heroui/react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";

import { formatDateTime } from "@/utils/utils";
import { getImmunoExamByDonationId } from "@/services/api";

type BloodType =
  | "A_POS"
  | "A_NEG"
  | "B_POS"
  | "B_NEG"
  | "AB_POS"
  | "AB_NEG"
  | "O_POS"
  | "O_NEG";

type IrregularAntibodies = "POSITIVE" | "NEGATIVE";
type ExamStatus = "UNDER_ANALYSIS" | "APPROVED" | "REJECTED";

interface ImmunohematologyExam {
  id: string;
  bloodType: BloodType;
  irregularAntibodies: IrregularAntibodies;
  observations: string;
  examStatus: ExamStatus;
  donation: {
    id: string;
    status: string;
  };
  createdAt: string;
  updatedAt: string;
}

export default function ViewImmunoExam() {
  const [exam, setExam] = useState<ImmunohematologyExam | null>(null);
  const [error, setError] = useState("");
  const uParams = useParams();
  const donationId = uParams.donationId as string;

  useEffect(() => {
    if (!donationId) {
      setError("Donation id not provided.");

      return;
    }

    getImmunoExamByDonationId(donationId)
      .then((exam) => setExam(exam))
      .catch((err) => {
        setError("Error searching for exam.");
      });
  }, [donationId]);

  if (error) return <p className="text-center text-red-500 mt-10">{error}</p>;
  if (!exam) return <p className="text-center mt-10">Loading exam...</p>;

  return (
    <div className="flex justify-center items-center min-h-screen">
      <Card className="max-w-xl w-full">
        <CardHeader className="text-xl font-bold">
          Visualize Immunohematological Exam
        </CardHeader>
        <Divider />
        <CardBody className="flex flex-col gap-4 text-base">
          <div id="exam-id">
            <strong>Exam ID:</strong> {exam.id}
          </div>
          <div id="exam-status">
            <strong>Status:</strong> {exam.examStatus}
          </div>
          <div id="blood-type">
            <strong>Blood type:</strong>{" "}
            {exam.bloodType
              ?.replace("_", " ")
              .replace("POS", "+")
              .replace("NEG", "-") || "Not informed"}
          </div>
          <div id="irregular-antibodies">
            <strong>Irregular Antibodies:</strong>{" "}
            {exam.irregularAntibodies === "NEGATIVE" ? "Negative" : "Positive"}
          </div>
          <div id="observations">
            <strong>Observations:</strong>{" "}
            {exam.observations ?? "No observations"}
          </div>
          <div id="created-at">
            <strong>Created at:</strong> {formatDateTime(exam.createdAt)}
          </div>
          <div id="updated-at">
            <strong>Updated at:</strong> {formatDateTime(exam.updatedAt)}
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
