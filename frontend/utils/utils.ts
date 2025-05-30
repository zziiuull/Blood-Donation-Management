import { BloodType, DonationStatus, ExamStatus } from "@/types";

export const formatDateTime = (isoString: string) => {
  const date = new Date(isoString);

  return date.toLocaleString("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

export const formatDate = (isoString: string) => {
  const date = new Date(isoString);

  return date.toLocaleString("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
};

export const calculateAge = (isoString: string): number => {
  const birthDate = new Date(isoString);
  const today = new Date();

  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDiff = today.getMonth() - birthDate.getMonth();

  if (
    monthDiff < 0 ||
    (monthDiff == 0 && today.getDate() < birthDate.getDate())
  ) {
    age--;
  }

  return age;
};

export const bloodTypeMap: Map<BloodType, string> = new Map([
  [BloodType.A_POS, "A+"],
  [BloodType.A_NEG, "A-"],
  [BloodType.B_POS, "B+"],
  [BloodType.B_NEG, "B-"],
  [BloodType.AB_POS, "AB+"],
  [BloodType.AB_NEG, "AB-"],
  [BloodType.O_POS, "O+"],
  [BloodType.O_NEG, "O-"],
]);

export const donationStatusMap: Map<DonationStatus, string> = new Map([
  [DonationStatus.APPROVED, "APPROVED"],
  [DonationStatus.REJECTED, "REJECTED"],
  [DonationStatus.UNDER_ANALYSIS, "UNDER ANALYSIS"],
]);

export const examStatusMap: Map<ExamStatus, string> = new Map([
  [ExamStatus.APPROVED, "APPROVED"],
  [ExamStatus.REJECTED, "REJECTED"],
  [ExamStatus.UNDER_ANALYSIS, "UNDER ANALYSIS"],
]);
