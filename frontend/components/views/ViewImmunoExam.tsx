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
      setError("Parâmetro donationId não fornecido.");

      return;
    }

    getImmunoExamByDonationId(donationId)
      .then((exam) => setExam(exam))
      .catch((err) => {
        console.error(err);
        setError("Erro ao buscar o exame.");
      });
  }, [donationId]);

  if (error) return <p className="text-center text-red-500 mt-10">{error}</p>;
  if (!exam) return <p className="text-center mt-10">Carregando exame...</p>;
  console.log(exam);

  return (
    <div className="flex justify-center items-center min-h-screen">
      <Card className="max-w-xl w-full">
        <CardHeader className="text-xl font-bold">
          Visualizar Exame Imuno-Hematológico
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
            <strong>Tipo Sanguíneo:</strong>{" "}
            {exam.bloodType
              ?.replace("_", " ")
              .replace("POS", "+")
              .replace("NEG", "-") || "Não informado"}
          </div>
          <div>
            <strong>Anticorpos Irregulares:</strong>{" "}
            {exam.irregularAntibodies === "NEGATIVE" ? "Negativo" : "Positivo"}
          </div>
          <div>
            <strong>Observações:</strong>{" "}
            {exam.observations ?? "Sem observações"}
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
