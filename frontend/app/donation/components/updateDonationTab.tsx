import type { Donation } from "@/types";

import { Icon } from "@iconify/react";
import React, { useState } from "react";
import { Button } from "@heroui/react";

import { DonationAutocomplete } from "./donationAutocomplete";
import DonorDetailsTable from "./donorDetailsTable";
import ImmunohematologyDetailsTable from "./immunohematologyDetailsTable";
import SerologicalDetailsTable from "./serologicalExamDetailsTable";

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

  const fetchUpdatedDonation = async (donationId: string) => {
    try {
      const donations = await loadDonations();
      const updatedDonation = donations.find(
        (donation) => donation.id === donationId,
      );

      if (updatedDonation) {
        setSelectedDonation(updatedDonation);
      }
    } catch (error) {
      showFailToast("Error fetching updated donation:" + error.message);
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
              />
            ) : (
              <Button
                className="lg:w-1/4 break-all"
                color="primary"
                isLoading={isLoadingImmunoExam}
                variant="faded"
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
              />
            ) : (
              <Button
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
            selectedDonation.serologicalScreeningExam && (
              <>
                <div className="flex items-center justify-center gap-2">
                  <Button
                    color="primary"
                    startContent={
                      <Icon className="text-xl" icon="lucide:check" />
                    }
                    onPress={() => handleApproveDonation(selectedDonation.id)}
                  >
                    Approve
                  </Button>
                  <Button
                    color="danger"
                    startContent={<Icon className="text-xl" icon="lucide:x" />}
                    onPress={() => handleRejectDonation(selectedDonation.id)}
                  >
                    Reject
                  </Button>
                </div>
              </>
            )}
        </>
      )}
    </div>
  );
}
