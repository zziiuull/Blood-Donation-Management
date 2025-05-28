import axios from 'axios';

export const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function getImmunoExamByDonationId(donationId: string) {
  const res = await axios.get(`${API_URL}/exams/immunohematology/${donationId}`);
  return res.data;
}

export async function updateImmunoExam(examId: string, data: any, approve = true) {
  const endpoint = approve ? 'approve' : 'reject';
  const res = await axios.post(`${API_URL}/exams/immunohematology/${examId}/${endpoint}`, data);
  return res.data;
}

export async function getSerologicalExamByDonationId(donationId: string) {
  const res = await axios.get(`${API_URL}/exams/serological-screening/${donationId}`);
  return res.data;
}

export async function updateSerologicalExam(examId: string, data: any, approve = true) {
  const endpoint = approve ? 'approve' : 'reject';
  const res = await axios.post(`${API_URL}/exams/serological-screening/${examId}/${endpoint}`, data);
  return res.data;
}