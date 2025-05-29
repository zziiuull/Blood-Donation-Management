import type { Donation } from "@/types";

import React, { useState } from "react";

import { DonationAutocomplete } from "./donationAutocomplete";
import DonorDetailsTable from "./donorDetailsTable";
import ImmunohematologyDetailsTable from "./immunohematologyDetailsTable";
import SerologicalDetailsTable from "./serologicalExamDetailsTable";

import { donationStatusMap } from "@/utils/utils";

export default function ViewDonationTab() {
  const [selectedDonation, setSelectedDonation] = useState<Donation | null>(
    null,
  );

  const handleDonationSelect = (donation: Donation | null) => {
    setSelectedDonation(donation);
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
              {donationStatusMap.get(selectedDonation?.status!)}
            </h3>
            <h3 className="text-lg font-bold text-default-600">Donor</h3>
            {selectedDonation?.donor && (
              <DonorDetailsTable donor={selectedDonation.donor} />
            )}
          </div>
          <div className="flex flex-col gap-4">
            <h3 className="text-lg font-bold text-default-600">
              Immunohematology exam
            </h3>
            <ImmunohematologyDetailsTable
              exam={selectedDonation.immunohematologyExam}
              type="view"
            />
            <h3 className="text-lg font-bold text-default-600">
              Serological screening exam
            </h3>
            <SerologicalDetailsTable
              exam={selectedDonation.serologicalScreeningExam}
              type="view"
            />
          </div>
        </>
      )}
    </div>
  );
}
