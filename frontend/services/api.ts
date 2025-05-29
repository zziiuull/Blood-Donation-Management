import axios from "./axios";

export const API_URL = "/api/v1";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");

  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};

export async function getImmunoExamByDonationId(donationId: string) {
  const res = await axios.get(
    `${API_URL}/exam/view/immunohematology/${donationId}`,
    getAuthHeaders(),
  );

  return res.data;
}

export async function updateImmunoExam(exam: any, data: any, approve = true) {
  const endpoint = approve ? "approve" : "reject";
  const res = await axios.post(
    `${API_URL}/exam/register/donation/${exam.donationId}/immunohematology/${endpoint}/${exam.id}`,
    data,
    getAuthHeaders(),
  );

  return res.data;
}

export async function getSerologicalExamByDonationId(donationId: string) {
  const res = await axios.get(
    `${API_URL}/exam/view/serologicalscreening/${donationId}`,
    getAuthHeaders(),
  );

  return res.data;
}

export async function updateSerologicalExam(
  exam: any,
  data: any,
  approve = true,
) {
  const endpoint = approve ? "approve" : "reject";
  const res = await axios.post(
    `${API_URL}/exam/register/donation/${exam.donationId}/serologicalscreening/${endpoint}/${exam.id}`,
    data,
    getAuthHeaders(),
  );

  return res.data;
}
