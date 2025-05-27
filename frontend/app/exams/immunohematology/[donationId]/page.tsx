'use client';

import { Input, Select, SelectItem, Button, Textarea } from '@heroui/react';
import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import {
  getImmunoExamByDonationId,
  updateImmunoExam,
} from '@/services/api';
import { BloodType, IrregularAntibodies } from '@/types/exam';

export default function UpdateImmunoExamPage() {
  const { donationId } = useParams();
  const router = useRouter();

  const [exam, setExam] = useState<any>(null);
  const [bloodType, setBloodType] = useState('');
  const [irregularAntibodies, setIrregularAntibodies] = useState('');
  const [observations, setObservations] = useState('');

  useEffect(() => {
    if (donationId) {
      getImmunoExamByDonationId(donationId as string).then((data) => {
        setExam(data);
        setBloodType(data.bloodType ?? '');
        setIrregularAntibodies(data.irregularAntibodies ?? '');
        setObservations(data.observations ?? '');
      });
    }
  }, [donationId]);

   if (!exam) return <p className="mt-10 text-center">Carregando exame...</p>;

  const handleSubmit = async (approve: boolean) => {
    try {
      await updateImmunoExam(exam.id, {
        bloodType,
        irregularAntibodies,
        observations,
      }, approve);

      alert(approve ? 'Exame aprovado!' : 'Exame rejeitado!');
      router.push('/exams');
    } catch (error) {
      alert('Erro ao atualizar exame');
      console.error(error);
    }
  };

   return (
    <div className="max-w-lg mx-auto mt-10 space-y-6">
      <h1 className="text-2xl font-semibold">Atualizar Exame Imunohematológico</h1>

      <Select
        label="Tipo sanguíneo"
        selectedKeys={[bloodType]}
        onSelectionChange={(keys) => setBloodType(Array.from(keys)[0] as string)}
        isRequired
      >
        {['A_POS', 'A_NEG', 'B_POS', 'B_NEG', 'AB_POS', 'AB_NEG', 'O_POS', 'O_NEG'].map((type) => (
          <SelectItem key={type}>{type.replace('_', ' ')}</SelectItem>
        ))}
      </Select>

      <Select
        label="Anticorpos Irregulares"
        selectedKeys={[irregularAntibodies]}
        onSelectionChange={(keys) => setIrregularAntibodies(Array.from(keys)[0] as string)}
        isRequired
      >
        <SelectItem key="NEGATIVE">Negativo</SelectItem>
        <SelectItem key="POSITIVE">Positivo</SelectItem>
      </Select>

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