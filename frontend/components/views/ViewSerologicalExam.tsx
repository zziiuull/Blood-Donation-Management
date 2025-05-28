'use client';

import {
  Card,
  CardBody,
  CardHeader,
  Divider,
} from '@heroui/react';
import { useEffect, useState } from 'react';

type DiseaseDetection = 'POSITIVE' | 'NEGATIVE';
type ExamStatus = 'UNDER_ANALYSIS' | 'APPROVED' | 'REJECTED';

interface SerologicalScreeningExam {
  id: string;
  status: ExamStatus;
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

  useEffect(() => {
    const mockExam: SerologicalScreeningExam = {
      id: 'mock-serological-id',
      status: 'REJECTED',
      hepatitisB: 'POSITIVE',
      hepatitisC: 'NEGATIVE',
      chagasDisease: 'POSITIVE',
      syphilis: 'NEGATIVE',
      aids: 'POSITIVE',
      htlv1_2: 'POSITIVE',
      observations: 'Paciente com múltiplas infecções detectadas.',
      createdAt: '2025-05-27T10:00:00',
      updatedAt: '2025-05-28T12:15:00',
      donation: {
        id: 'mock-donation-id',
        status: 'UNDER_ANALYSIS',
      },
    };

    setExam(mockExam);
  }, []);

  if (!exam) return <p className="text-center mt-10">Carregando exame...</p>;

  return (
    <div className="flex justify-center items-center min-h-screen">
      <Card className="max-w-xl w-full">
        <CardHeader className="text-xl font-bold">Visualizar Exame Sorológico</CardHeader>
        <Divider />
        <CardBody className="flex flex-col gap-4 text-base">
          <div><strong>ID do Exame:</strong> {exam.id}</div>
          <div><strong>Status:</strong> {exam.status}</div>
          <div><strong>Hepatite B:</strong> {exam.hepatitisB}</div>
          <div><strong>Hepatite C:</strong> {exam.hepatitisC}</div>
          <div><strong>Doença de Chagas:</strong> {exam.chagasDisease}</div>
          <div><strong>Sífilis:</strong> {exam.syphilis}</div>
          <div><strong>AIDS:</strong> {exam.aids}</div>
          <div><strong>HTLV 1/2:</strong> {exam.htlv1_2}</div>
          <div><strong>Observações:</strong> {exam.observations || 'Sem observações'}</div>
          <div><strong>Data de criação:</strong> {new Date(exam.createdAt).toLocaleString()}</div>
          <div><strong>Última atualização:</strong> {new Date(exam.updatedAt).toLocaleString()}</div>
        </CardBody>
      </Card>
    </div>
  );
}
