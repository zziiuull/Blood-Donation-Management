"use client";

import React, { useEffect, useState } from "react";
import { Tabs, Tab } from "@heroui/react";
import { useRouter } from "next/navigation";

import RegisterDonationTab from "./components/registerDonationTab";
import UpdateDonationTab from "./components/updateDonationTab";
import ViewDonationTab from "./components/viewDonationTab";

export default function Donation() {
  const router = useRouter();

  const [activeTab, setActiveTab] = useState("register");

  const handleTabChange = (key: React.Key) => {
    setActiveTab(key as string);
  };

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) router.push("/login");
  });

  return (
    <div>
      <h1 className="text-3xl font-bold mb-5">Donations</h1>
      <div className="border border-gray-300 rounded-2xl w-full h-[500px] overflow-hidden p-4 mb-4 flex flex-col gap-4">
        <div className="flex-1 flex gap-2 min-h-0 rounded-2xl p-2">
          <div className="flex-1 flex flex-col p-4 overflow-auto">
            <Tabs onSelectionChange={handleTabChange}>
              <Tab key="register" title="Register donation">
                <RegisterDonationTab />
              </Tab>

              <Tab key="update" title="Update donation">
                <UpdateDonationTab />
              </Tab>

              <Tab key="view" title="View donation">
                <ViewDonationTab />
              </Tab>
            </Tabs>
          </div>
        </div>
      </div>
    </div>
  );
}
