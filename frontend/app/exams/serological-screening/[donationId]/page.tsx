'use client';

import {
  Select,
  SelectItem,
  Textarea,
  Button,
} from '@heroui/react';
import { useParams, useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import {
  getSerologicalExamByDonationId,
  updateSerologicalExam,
} from '@/services/api';
import { DiseaseDetection } from '@/types/exam';

export default function UpdateSerologicalExamPage() {
  const { donationId } = useParams();
  const router = useRouter();

  const [exam, setExam] = useState<any>(null);
  const [observations, setObservations] = useState('');
  const [fields, setFields] = useState({
    hepatitisB: '',
    hepatitisC: '',
    chagasDisease: '',
    syphilis: '',
    aids: '',
    htlv1_2: '',
  });

  // useEffect(() => {
  //   if (donationId) {
  //     getSerologicalExamByDonationId(donationId as string).then((data) => {
  //       setExam(data);
  //       setObservations(data.observations ?? '');
  //       setFields({
  //         hepatitisB: data.hepatitisB ?? '',
  //         hepatitisC: data.hepatitisC ?? '',
  //         chagasDisease: data.chagasDisease ?? '',
  //         syphilis: data.syphilis ?? '',
  //         aids: data.aids ?? '',
  //         htlv1_2: data.htlv1_2 ?? '',
  //       });
  //     });
  //   }
  // }, [donationId]);

  useEffect(() => {
    const mockExam = {
      id: 'mock-id-123',
      observations: 'Paciente saudável.',
      hepatitisB: 'NEGATIVE',
      hepatitisC: 'NEGATIVE',
      chagasDisease: 'NEGATIVE',
      syphilis: 'NEGATIVE',
      aids: 'NEGATIVE',
      htlv1_2: 'NEGATIVE',
      donation: {
        id: 'mock-donation-id',
        status: 'UNDER_ANALYSIS',
      },
      status: 'UNDER_ANALYSIS',
    };
  
    setExam(mockExam);
    setObservations(mockExam.observations);
    setFields({
      hepatitisB: mockExam.hepatitisB,
      hepatitisC: mockExam.hepatitisC,
      chagasDisease: mockExam.chagasDisease,
      syphilis: mockExam.syphilis,
      aids: mockExam.aids,
      htlv1_2: mockExam.htlv1_2,
    });
  }, []);

  const handleChange = (key: string, value: string) => {
    setFields((prev) => ({ ...prev, [key]: value }));
  };

  const handleSubmit = async (approve: boolean) => {
    try {
      await updateSerologicalExam(exam.id, { ...fields, observations }, approve);
      alert(approve ? 'Exame aprovado!' : 'Exame rejeitado!');
      router.push('/exams');
    } catch (error) {
      console.error(error);
      alert('Erro ao atualizar exame');
    }
  };

  if (!exam) return <p className="mt-10 text-center">Carregando exame...</p>;

  return (
    <div className="max-w-lg mx-auto mt-10 space-y-6">
      <h1 className="text-2xl font-semibold">Atualizar Exame de Triagem Sorológica</h1>

      {Object.entries(fields).map(([key, value]) => (
        <Select
          key={key}
          label={key.replace(/([A-Z])/g, ' $1').toUpperCase()}
          selectedKeys={[value]}
          onSelectionChange={(keys) => handleChange(key, Array.from(keys)[0] as string)}
          isRequired
        >
          <SelectItem key="NEGATIVE">Negativo</SelectItem>
          <SelectItem key="POSITIVE">Positivo</SelectItem>
        </Select>
      ))}

      <Textarea
        label="Observações"
        value={observations}
        onChange={(e) => setObservations(e.target.value)}
        placeholder="Observações clínicas relevantes..."
      />

      <div className="flex justify-between gap-4">
        <Button color="danger" onPress={() => handleSubmit(false)}>Reprovar</Button>
        <Button color="primary" onPress={() => handleSubmit(true)}>Aprovar</Button>
      </div>
    </div>
  );
}