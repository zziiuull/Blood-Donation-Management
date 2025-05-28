// 'use client';

// import { useState } from "react";
// import { Input, Textarea } from "@heroui/input";
// import { Select, SelectItem } from "@heroui/react";
// import { Button } from "@heroui/react";
// import { Card } from "@heroui/react";
// import { BloodType, IrregularAntibodies } from '@/types/exam';
// //import { registerImmunoExam } from '@/services/api';

// export default function ImmunoExamForm() {
//     const [donationId, setDonationId] = useState('');
//     const [bloodType, setBloodType] = useState('');
//     const [irregularAntibodies, setIrregularAntibodies] = useState('');
//     const [observations, setObservations] = useState('');

//     const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     try {
//       await registerImmunoExam(donationId, {
//         bloodType,
//         irregularAntibodies,
//         observations,
//       });
//       alert('Exame cadastrado com sucesso!');
//     } catch (error) {
//       alert('Erro ao cadastrar exame');
//     }
//   };

//   return (
//     <Card className="max-w-lg mx-auto mt-10 p-6 space-y-6">
//       <h1 className="text-xl font-semibold">Cadastro de Exame Imunohematológico</h1>
//       <form onSubmit={handleSubmit} className="space-y-4">
//         <Input label="ID da Doação" value={donationId} onChange={e => setDonationId(e.target.value)} isRequired />

//         <Select label="Tipo Sanguíneo" value={bloodType} onChange={e => setBloodType(e.target.value)} isRequired>
//           <SelectItem key="">Selecione</SelectItem>
//           <SelectItem key="A_POS">A+</SelectItem>
//           <SelectItem key="A_NEG">A-</SelectItem>
//           <SelectItem key="B_POS">B+</SelectItem>
//           <SelectItem key="B_NEG">B-</SelectItem>
//           <SelectItem key="O_POS">O+</SelectItem>
//           <SelectItem key="O_NEG">O-</SelectItem>
//           <SelectItem key="AB_POS">AB+</SelectItem>
//           <SelectItem key="AB_NEG">AB-</SelectItem>
//         </Select>

//         <Select label="Anticorpos Irregulares" value={irregularAntibodies} onChange={e => setIrregularAntibodies(e.target.value)} isRequired>
//           <SelectItem key="">Selecione</SelectItem>
//           <SelectItem key="POSITIVE">Positivo</SelectItem>
//           <SelectItem key="NEGATIVE">Negativo</SelectItem>
//         </Select>

//         <Textarea
//           label="Observações"
//           value={observations}
//           onChange={e => setObservations(e.target.value)}
//           placeholder="Alguma observação clínica relevante..."
//         />

//         <Button type="submit" color="primary" className="w-full">
//           Cadastrar Exame
//         </Button>
//       </form>
//     </Card>
//   );
// }