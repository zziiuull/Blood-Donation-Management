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