import axios from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL;

export async function getImmunoExamByDonationId(donationId: string) {
  const res = await axios.get(`${API_URL}/exams/immunohematology/${donationId}`);
  return res.data;
}
