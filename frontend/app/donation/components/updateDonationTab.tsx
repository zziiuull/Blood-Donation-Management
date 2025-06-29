import { Icon } from "@iconify/react";
import React, { useEffect, useState } from "react";
import { Button } from "@heroui/react";
import { useSearchParams } from "next/navigation";

import { DonationAutocomplete } from "./donationAutocomplete";
import DonorDetailsTable from "./donorDetailsTable";
import ImmunohematologyDetailsTable from "./immunohematologyDetailsTable";
import SerologicalDetailsTable from "./serologicalExamDetailsTable";

import { DonationStatus, ExamStatus, type Donation } from "@/types";
import axios from "@/services/axios";
import { donationStatusMap } from "@/utils/utils";
import showFailToast from "@/services/toast/showFailToast";
import showSuccessToast from "@/services/toast/showSuccessToast";

export default function UpdateDonationTab() {
  const [selectedDonation, setSelectedDonation] = useState<Donation | null>(
    null,
  );
  const [isLoadingImmunoExam, setIsLoadingImmunoExam] =
    useState<boolean>(false);
  const [isLoadingSeroExam, setIsLoadingSeroExam] = useState<boolean>(false);
  const searchParams = useSearchParams();

  const loadDonations = async () => {
    try {
      const result = await axios.get<Donation[]>("/api/v1/donation", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });

      if (result.status === 200) return result.data;
    } catch (error) {
      return [];
    }
  };

  useEffect(() => {
    const fetchDonations = async () => {
      const donations = await loadDonations();
      const donationId = searchParams.get("donationId");

      if (donationId) {
        const foundDonation = donations?.find((d) => d.id === donationId);

        setSelectedDonation(foundDonation ?? null);
      }
    };

    fetchDonations();
  }, [searchParams]);

  const fetchUpdatedDonation = async (donationId: string) => {
    try {
      const donations = await loadDonations();
      const updatedDonation = donations?.find(
        (donation) => donation.id === donationId,
      );

      if (updatedDonation) {
        setSelectedDonation(updatedDonation);
      }
    } catch (error) {
      if (error.response.data.message) {
        showFailToast(error.response.data.message);
      } else {
        showFailToast(error.message);
      }
    }
  };

  const handleDonationSelect = (donation: Donation | null) => {
    setSelectedDonation(donation);
  };

  const handleImmunoExamRequest = async (donationId: string) => {
    setIsLoadingImmunoExam(true);
    try {
      const result = await axios.post(
        `/api/v1/exam/request/immunohematology/${donationId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
      );

      if (result.status === 200) {
        showSuccessToast("Immunohematology exam requested");

        await fetchUpdatedDonation(donationId);
      }
    } catch (error) {
      if (error.response.data.message) {
        showFailToast(error.response.data.message);
      } else {
        showFailToast(error.message);
      }
    } finally {
      setIsLoadingImmunoExam(false);
    }
  };

  const handleSeroExamRequest = async (donationId: string) => {
    setIsLoadingSeroExam(true);
    try {
      const result = await axios.post(
        `/api/v1/exam/request/serologicalscreening/${donationId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
      );

      if (result.status === 200) {
        showSuccessToast("Serological screening exam requested");

        await fetchUpdatedDonation(donationId);
      }
    } catch (error) {
      if (error.response.data.message) {
        showFailToast(error.response.data.message);
      } else {
        showFailToast(error.message);
      }
    } finally {
      setIsLoadingSeroExam(false);
    }
  };

  const handleApproveDonation = async (donationId: string) => {
    try {
      const result = await axios.put(
        `/api/v1/donation/approve/${donationId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
      );

      if (result.status === 200) {
        showSuccessToast("Donation approved");

        await fetchUpdatedDonation(donationId);
      }
    } catch (error) {
      if (error.response.data.message) {
        showFailToast(error.response.data.message);
      } else {
        showFailToast(error.message);
      }
    }
  };

  const handleRejectDonation = async (donationId: string) => {
    try {
      const result = await axios.put(
        `/api/v1/donation/reject/${donationId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
      );

      if (result.status === 200) {
        showSuccessToast("Donation rejected");

        await fetchUpdatedDonation(donationId);
      }
    } catch (error) {
      if (error.response.data.message) {
        showFailToast(error.response.data.message);
      } else {
        showFailToast(error.message);
      }
    }
  };

  return (
    <div className="flex flex-col gap-4">
      <div className="flex-shrink-0">
        <DonationAutocomplete handleDonationSelect={handleDonationSelect} />
      </div>

      {selectedDonation && (
        <>
          <div className="flex flex-col gap-4">
            <h3 className="text-xl font-bold text-default-600">
              Donation status:{" "}
              {donationStatusMap.get(selectedDonation?.status as any) ?? "N/A"}
            </h3>

            <h3 className="text-lg font-bold text-default-600">Donor</h3>
            {selectedDonation.donor && (
              <DonorDetailsTable donor={selectedDonation.donor} />
            )}
          </div>

          <div className="flex flex-col gap-4">
            <h3 className="text-lg font-bold text-default-600">
              Immunohematology exam
            </h3>
            {selectedDonation.immunohematologyExam ? (
              <ImmunohematologyDetailsTable
                exam={selectedDonation.immunohematologyExam}
                type="update"
              />
            ) : (
              <Button
                className="lg:w-1/4 break-all"
                color="primary"
                isLoading={isLoadingImmunoExam}
                variant="faded"
                id="request-immunohematology-exam-button"
                onPress={() => handleImmunoExamRequest(selectedDonation.id)}
              >
                {isLoadingImmunoExam
                  ? "Loading..."
                  : "Request Immunohematology Exam"}
              </Button>
            )}

            <h3 className="text-lg font-bold text-default-600">
              Serological screening exam
            </h3>
            {selectedDonation.serologicalScreeningExam ? (
              <SerologicalDetailsTable
                exam={selectedDonation.serologicalScreeningExam}
                type="update"
              />
            ) : (
              <Button
              id="request-serologicalscreening-exam-button"
                className="lg:w-1/4 break-all"
                color="primary"
                isLoading={isLoadingSeroExam}
                variant="faded"
                onPress={() => handleSeroExamRequest(selectedDonation.id)}
              >
                {isLoadingSeroExam
                  ? "Loading..."
                  : "Request Serological Screening Exam"}
              </Button>
            )}
          </div>

          {selectedDonation.immunohematologyExam &&
            selectedDonation.serologicalScreeningExam &&
            selectedDonation.serologicalScreeningExam.status !=
              ExamStatus.UNDER_ANALYSIS &&
            selectedDonation.immunohematologyExam.status !=
              ExamStatus.UNDER_ANALYSIS &&
            selectedDonation.status ==
              DonationStatus.UNDER_ANALYSIS && (
                <>
                  <div className="flex items-center justify-center gap-2">
                    <Button
                    id="approve-exam-button"
                      color="primary"
                      startContent={
                        <Icon className="text-xl" icon="lucide:check" />
                      }
                      onPress={() => handleApproveDonation(selectedDonation.id)}
                    >
                      Approve
                    </Button>
                    <Button
                    id="reject-exam-button"
                      color="danger"
                      startContent={
                        <Icon className="text-xl" icon="lucide:x" />
                      }
                      onPress={() => handleRejectDonation(selectedDonation.id)}
                    >
                      Reject
                    </Button>
                  </div>
                </>,
              )}
        </>
      )}
    </div>
  );
}
