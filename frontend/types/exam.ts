export type BloodType =
  | 'A_POS' | 'A_NEG'
  | 'B_POS' | 'B_NEG'
  | 'O_POS' | 'O_NEG'
  | 'AB_POS' | 'AB_NEG';

export type IrregularAntibodies = 'POSITIVE' | 'NEGATIVE';

export type DiseaseDetection = 'POSITIVE' | 'NEGATIVE';

export enum ExamStatus {
  UNDER_ANALYSIS = 'UNDER_ANALYSIS',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

export type ImmunohematologyExamResponse = {
  id: string;
  donationId: string;
  examStatus: 'UNDER_ANALYSIS' | 'APPROVED' | 'REJECTED';
  createdAt: string;
  updatedAt: string;
  observations: string | null;
  bloodType: 'A_POS' | 'A_NEG' | 'B_POS' | 'B_NEG' | 'AB_POS' | 'AB_NEG' | 'O_POS' | 'O_NEG';
  irregularAntibodies: 'POSITIVE' | 'NEGATIVE' | null;
};

export type SerologicalScreeningExamResponse = {
  id: string;
  donationId: string;
  examStatus: 'UNDER_ANALYSIS' | 'APPROVED' | 'REJECTED';
  createdAt: string;
  updatedAt: string;
  observations: string | null;
  hepatitisB: 'POSITIVE' | 'NEGATIVE' | null;
  hepatitisC: 'POSITIVE' | 'NEGATIVE' | null;
  chagasDisease: 'POSITIVE' | 'NEGATIVE' | null;
  syphilis: 'POSITIVE' | 'NEGATIVE' | null;
  aids: 'POSITIVE' | 'NEGATIVE' | null;
  htlv1_2: 'POSITIVE' | 'NEGATIVE' | null;
};
