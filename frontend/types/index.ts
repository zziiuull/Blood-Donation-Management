import { SVGProps } from "react";

export type IconSvgProps = SVGProps<SVGSVGElement> & {
  size?: number;
};

type ContactInfo = {
  email: string;
  phone: string;
  address: string;
};

export enum Sex {
  MALE = "MALE",
  FEMALE = "FEMALE",
}

export enum BloodType {
  A_POS = "A_POS",
  A_NEG = "A_NEG",
  B_POS = "B_POS",
  B_NEG = "B_NEG",
  AB_POS = "AB_POS",
  AB_NEG = "AB_NEG",
  O_POS = "O_POS",
  O_NEG = "O_NEG",
}

export type Donor = {
  id: string;
  name: string;
  contactInfo: ContactInfo;
  cpf: { number: string };
  weight: number;
  birthDate: string;
  sex: Sex;
  bloodType: BloodType;
};

export type DonorSearchCardProps = {
  donor: Donor;
  handleDonorSelect: (Donor: Donor) => void;
};

export type DonorSearchListContainerProps = {
  searchValue: string;
  handleDonorSelect: (Donor: Donor) => void;
};

export enum AppointmentStatus {
  SCHEDULED = "SCHEDULED",
  CANCELED = "CANCELED",
  COMPLETE = "COMPLETED",
}

type CollectionSite = {
  id: string;
  name: string;
};

export type Appointment = {
  id: string;
  appointmentDate: string;
  status: AppointmentStatus;
  collectionSite: CollectionSite;
  notes: string;
};

export type DonorDetailsProps = {
  donor: Donor;
};

export enum DonationStatus {
  APPROVED = "APPROVED",
  REJECTED = "REJECTED",
  UNDER_ANALYSIS = "UNDER_ANALYSIS",
}

export type Donation = {
  id: string;
  donor?: Donor;
  appointment?: Appointment;
  status?: DonationStatus;
  createdAt: string;
  updatedAt: string;
  immunohematologyExam?: ImmunohemalogyExam;
  serologicalScreeningExam?: SerologicalScreeningExam;
};

export enum ExamStatus {
  APPROVED = "APPROVED",
  REJECTED = "REJECTED",
  UNDER_ANALYSIS = "UNDER_ANALYSIS",
}
export type Exam = {
  id: string;
  name?: string;
  donation?: Donation;
  status: ExamStatus;
  createdAt?: string;
  updatedAt?: string;
  performedAt?: string;
  observations?: string;
};

export type ExamCardProps = {
  exam: Exam;
};

export enum IrregularAntibodies {
  POSITIVE = "POSTIVE",
  NEGATIVE = "NEGATIVE",
}

export enum DiseaseDetection {
  POSITIVE = "POSTIVE",
  NEGATIVE = "NEGATIVE",
}

export type ImmunohemalogyExam = Exam & {
  bloodType: BloodType;
  irregularAntibodies: IrregularAntibodies;
};

export type SerologicalScreeningExam = Exam & {
  hepatitisB: DiseaseDetection;
  hepatitisC: DiseaseDetection;
  chagasDisease: DiseaseDetection;
  syphilis: DiseaseDetection;
  aids: DiseaseDetection;
  htlv1_2: DiseaseDetection;
};
