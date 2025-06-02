"use client";

import { Card, CardBody, CardHeader, Divider } from "@heroui/react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";

import { formatDateTime } from "@/utils/utils";
import { getSerologicalExamByDonationId } from "@/services/api";

type DiseaseDetection = "POSITIVE" | "NEGATIVE";
type ExamStatus = "UNDER_ANALYSIS" | "APPROVED" | "REJECTED";

interface SerologicalScreeningExam {
  id: string;
  examStatus: ExamStatus;
  hepatitisB: DiseaseDetection;
  hepatitisC: DiseaseDetection;
  chagasDisease: DiseaseDetection;
  syphilis: DiseaseDetection;
  aids: DiseaseDetection;
  htlv1_2: DiseaseDetection;
  observations: string;
  createdAt: string;
  updatedAt: string;
  donation: {
    id: string;
    status: string;
  };
}

export default function ViewSerologicalExam() {
  const [exam, setExam] = useState<SerologicalScreeningExam | null>(null);
  const [error, setError] = useState("");
  const uParams = useParams();
  const donationId = uParams.donationId as string;

  useEffect(() => {
    if (!donationId) {
      setError("Donation id not provided.");

      return;
    }

    getSerologicalExamByDonationId(donationId)
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
          Visualize Serological Screening Exam
        </CardHeader>
        <Divider />
        <CardBody className="flex flex-col gap-4 text-base">
          <div id="exam-id">
            <strong>Exam ID:</strong> {exam.id}
          </div>
          <div id="exam-status">
            <strong>Status:</strong> {exam.examStatus}
          </div>
          <div id="hepatitis-b">
            <strong>Hepatitis B:</strong> {exam.hepatitisB}
          </div>
          <div id="hepatitis-c">
            <strong>Hepatitis C:</strong> {exam.hepatitisC}
          </div>
          <div id="chagas-disease">
            <strong>Chagas Disease:</strong> {exam.chagasDisease}
          </div>
          <div id="syphilis">
            <strong>Syphilis:</strong> {exam.syphilis}
          </div>
          <div id="aids">
            <strong>AIDS:</strong> {exam.aids}
          </div>
          <div id="htlv1-2">
            <strong>HTLV 1/2:</strong> {exam.htlv1_2}
          </div>
          <div id="observations">
            <strong>Observações:</strong>{" "}
            {exam.observations || "No observations"}
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
