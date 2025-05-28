'use client';

import {
  Card,
  CardBody,
  CardHeader,
  Divider,
} from '@heroui/react';
import { useEffect, useState } from 'react';

type BloodType =
  | 'A_POS' | 'A_NEG'
  | 'B_POS' | 'B_NEG'
  | 'AB_POS' | 'AB_NEG'
  | 'O_POS' | 'O_NEG';

type IrregularAntibodies = 'POSITIVE' | 'NEGATIVE';
type ExamStatus = 'UNDER_ANALYSIS' | 'APPROVED' | 'REJECTED';

interface ImmunohematologyExam {
  id: string;
  bloodType: BloodType;
  irregularAntibodies: IrregularAntibodies;
  observations: string;
  status: ExamStatus;
  donation: {
    id: string;
    status: string;
  };
  createdAt: string;
  updatedAt: string;
}

export default function ViewImmunoExam() {
  const [exam, setExam] = useState<ImmunohematologyExam | null>(null);

  useEffect(() => {
    // Dados simulados
    const mockExam: ImmunohematologyExam = {
      id: 'mock-id-123',
      bloodType: 'O_POS',
      irregularAntibodies: 'NEGATIVE',
      observations: 'Paciente sem alterações imunológicas.',
      status: 'APPROVED',
      createdAt: '2025-05-27T14:00:00',
      updatedAt: '2025-05-28T16:30:00',
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
        <CardHeader className="text-xl font-bold">Visualizar Exame Imuno-Hematológico</CardHeader>
        <Divider />
        <CardBody className="flex flex-col gap-4 text-base">
          <div><strong>ID do Exame:</strong> {exam.id}</div>
          <div><strong>Status:</strong> {exam.status}</div>
          <div><strong>Tipo Sanguíneo:</strong> {exam.bloodType.replace('_', ' ').replace('POS', '+').replace('NEG', '-')}</div>
          <div><strong>Anticorpos Irregulares:</strong> {exam.irregularAntibodies === 'NEGATIVE' ? 'Negativo' : 'Positivo'}</div>
          <div><strong>Observações:</strong> {exam.observations || 'Sem observações'}</div>
          <div><strong>Data de criação:</strong> {new Date(exam.createdAt).toLocaleString()}</div>
          <div><strong>Última atualização:</strong> {new Date(exam.updatedAt).toLocaleString()}</div>
        </CardBody>
      </Card>
    </div>
  );
}
