import type { Appointment, Donor } from "@/types";

import React, { useState } from "react";
import { Button } from "@heroui/react";

import { AppointmentAutocomplete } from "./appointmentAutocomplete";
import { DonorAutocomplete } from "./donorAutocomplete";
import { ExamCheckbox } from "./examCheckbox";

import axios from "@/services/axios";
import showFailToast from "@/services/toast/showFailToast";
import showSuccessToast from "@/services/toast/showSuccessToast";

export default function RegisterDonationTab() {
  const [selectedDonor, setSelectedDonor] = useState<Donor | null>(null);
  const [selectedAppointment, setSelectedAppointment] =
    useState<Appointment | null>(null);
  const [isSelectedImmunoExam, setIsSelectedImmunoExam] =
    useState<boolean>(false);
  const [isSelectedSeroExam, setIsSelectedSeroExam] = useState<boolean>(false);

  const handleDonorSelect = (donor: Donor | null) => {
    setSelectedDonor(donor);
  };

  const handleAppointmentSelect = (appointment: Appointment | null) => {
    setSelectedAppointment(appointment);
  };

  const handleImmunoExamRequest = async (donationId: string) => {
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
      }
    } catch (error) {
      if (error.response.data.message) {
        showFailToast(error.response.data.message);
      } else {
        showFailToast(error.message);
      }
    }
  };

  const handleSeroExamRequest = async (donationId: string) => {
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
      }
    } catch (error) {
      if (error.response.data.message) {
        showFailToast(error.response.data.message);
      } else {
        showFailToast(error.message);
      }
    }
  };

  const handleRequestDonation = async () => {
    try {
      const result = await axios.post(
        "/api/v1/donation",
        {
          donorId: selectedDonor?.id,
          appointmentId: selectedAppointment?.id,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
      );

      if (result.status === 200) {
        const donationId = result.data.id;

        showSuccessToast("Donation requested");

        if (isSelectedImmunoExam) {
          await handleImmunoExamRequest(donationId);
        }
        if (isSelectedSeroExam) {
          await handleSeroExamRequest(donationId);
        }

        setSelectedDonor(null);
        setSelectedAppointment(null);
        setIsSelectedImmunoExam(false);
        setIsSelectedSeroExam(false);
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
    <div className="flex flex-col gap-4 mt-4">
      <div className="flex flex-col gap-10 justify-evenly">
        <div>
          <h3 className="text-lg font-bold text-default-600">
            Step 1: Select a donor
          </h3>
          <DonorAutocomplete handleDonorSelect={handleDonorSelect} />
        </div>
        {selectedDonor != null && (
          <div>
            <h3 className="text-lg font-bold text-default-600">
              Step 2: Select an appointment
            </h3>
            <AppointmentAutocomplete
              handleAppointmentSelect={handleAppointmentSelect}
            />
          </div>
        )}
        {selectedAppointment != null && selectedDonor != null && (
          <div className="flex flex-col gap-2">
            <h3 className="text-lg font-bold text-default-600">
              Step 3: Select required exams
            </h3>
            <div className="flex flex-col md:flex-row gap-8">
              <ExamCheckbox
                description="Blood typing and compatibility testing"
                isSelected={isSelectedImmunoExam}
                name="Immunohematology exam"
                onChange={setIsSelectedImmunoExam}
              />
              <ExamCheckbox
                description="Infectious disease screening"
                isSelected={isSelectedSeroExam}
                name="Serological Screening exam"
                onChange={setIsSelectedSeroExam}
              />
            </div>
          </div>
        )}
      </div>
      {selectedAppointment != null && selectedDonor != null && (
        <Button
          className="mt-4"
          id="register-donation-button"
          variant="faded"
          onPress={handleRequestDonation}
        >
          Register donation
        </Button>
      )}
    </div>
  );
}
