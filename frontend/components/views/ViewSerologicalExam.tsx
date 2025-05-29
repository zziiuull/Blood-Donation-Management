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
      setError("Parâmetro donationId não fornecido.");

      return;
    }

    getSerologicalExamByDonationId(donationId)
      .then((exam) => setExam(exam))
      .catch((err) => {
        console.error(err);
        setError("Erro ao buscar o exame.");
      });
  }, [donationId]);

  if (error) return <p className="text-center text-red-500 mt-10">{error}</p>;
  if (!exam) return <p className="text-center mt-10">Carregando exame...</p>;

  return (
    <div className="flex justify-center items-center min-h-screen">
      <Card className="max-w-xl w-full">
        <CardHeader className="text-xl font-bold">
          Visualizar Exame Sorológico
        </CardHeader>
        <Divider />
        <CardBody className="flex flex-col gap-4 text-base">
          <div>
            <strong>ID do Exame:</strong> {exam.id}
          </div>
          <div>
            <strong>Status:</strong> {exam.examStatus}
          </div>
          <div>
            <strong>Hepatite B:</strong> {exam.hepatitisB}
          </div>
          <div>
            <strong>Hepatite C:</strong> {exam.hepatitisC}
          </div>
          <div>
            <strong>Doença de Chagas:</strong> {exam.chagasDisease}
          </div>
          <div>
            <strong>Sífilis:</strong> {exam.syphilis}
          </div>
          <div>
            <strong>AIDS:</strong> {exam.aids}
          </div>
          <div>
            <strong>HTLV 1/2:</strong> {exam.htlv1_2}
          </div>
          <div>
            <strong>Observações:</strong>{" "}
            {exam.observations || "Sem observações"}
          </div>
          <div>
            <strong>Data de criação:</strong> {formatDateTime(exam.createdAt)}
          </div>
          <div>
            <strong>Última atualização:</strong>{" "}
            {formatDateTime(exam.updatedAt)}
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
