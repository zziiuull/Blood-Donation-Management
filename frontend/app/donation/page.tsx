"use client";

import type { Donation } from "@/types";

import { Icon } from "@iconify/react";
import React, { useState } from "react";
import { Tabs, Tab, Button } from "@heroui/react";

import { DonationAutocomplete } from "./components/donationAutocomplete";
import DonorDetailsTable from "./components/donorDetailsTable";
import ImmunohematologyDetailsTable from "./components/immunohematologyDetailsTable";
import { immunohemalogyExam, serologicalExam } from "./components/exams";
import SerologicalDetailsTable from "./components/serologicalExamDetailsTable";

export default function Donation() {
  const [selectedDonation, setSelectedDonation] = useState<Donation | null>(
    null,
  );

  const [activeTab, setActiveTab] = useState("update");

  const handleDonationSelect = (donation: Donation | null) => {
    setSelectedDonation(donation);
  };

  const shouldShowSearch = activeTab === "update" || activeTab === "view";

  return (
    <div>
      <h1 className="text-3xl font-bold mb-5">Donations</h1>
      <div className="border border-gray-300 rounded-2xl w-full h-[500px] overflow-hidden p-4 mb-4 flex flex-col gap-4">
        <div className="flex-1 flex gap-2 min-h-0 rounded-2xl p-2">
          <div className="flex-1 flex flex-col p-4 overflow-auto">
            <Tabs
              onSelectionChange={(key: React.Key) => {
                setActiveTab(key as string);
                setSelectedDonation(null);
              }}
            >
              <Tab
                key="update"
                className="flex flex-col gap-4"
                title="Update donation"
              >
                {shouldShowSearch && (
                  <div className="flex-shrink-0">
                    <DonationAutocomplete
                      handleDonationSelect={handleDonationSelect}
                    />
                  </div>
                )}
                {selectedDonation && (
                  <>
                    <div className="flex flex-col gap-4">
                      <h3 className="text-lg font-bold text-default-600">
                        Donor
                      </h3>
                      <DonorDetailsTable donor={selectedDonation.donor} />
                    </div>
                    <div className="flex flex-col gap-4">
                      <h3 className="text-lg font-bold text-default-600">
                        Immunohematology exam
                      </h3>
                      <ImmunohematologyDetailsTable exam={immunohemalogyExam} />
                      <h3 className="text-lg font-bold text-default-600">
                        Serological screening exam
                      </h3>
                      <SerologicalDetailsTable exam={serologicalExam} />
                    </div>
                    <div className="flex items-center justify-center gap-2">
                      <Button
                        color="primary"
                        startContent={
                          <Icon className="text-xl " icon="lucide:check" />
                        }
                      >
                        Approve
                      </Button>
                      <Button
                        color="danger"
                        startContent={
                          <Icon className="text-xl" icon="lucide:x" />
                        }
                      >
                        Reject
                      </Button>
                    </div>
                  </>
                )}
              </Tab>

              <Tab
                key="view"
                className="flex flex-col gap-4"
                title="View donation"
              >
                {shouldShowSearch && (
                  <div className="flex-shrink-0">
                    <DonationAutocomplete
                      handleDonationSelect={handleDonationSelect}
                    />
                  </div>
                )}
                {selectedDonation && (
                  <>
                    <div className="flex flex-col gap-4">
                      <h3 className="text-lg font-bold text-default-600">
                        Donor
                      </h3>
                      <DonorDetailsTable donor={selectedDonation?.donor} />
                    </div>
                    <div className="flex flex-col gap-4">
                      <h3 className="text-lg font-bold text-default-600">
                        Immunohematology exam
                      </h3>
                      <ImmunohematologyDetailsTable exam={immunohemalogyExam} />
                      <h3 className="text-lg font-bold text-default-600">
                        Serological screening exam
                      </h3>
                      <SerologicalDetailsTable exam={serologicalExam} />
                    </div>
                  </>
                )}
              </Tab>
            </Tabs>
          </div>
        </div>
      </div>
    </div>
  );
}
