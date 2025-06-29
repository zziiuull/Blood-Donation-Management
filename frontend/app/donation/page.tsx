"use client";

import React, { useEffect, useState } from "react";
import { Tabs, Tab } from "@heroui/react";
import { useRouter } from "next/navigation";
import { useSearchParams } from "next/navigation";

import RegisterDonationTab from "./components/registerDonationTab";
import UpdateDonationTab from "./components/updateDonationTab";
import ViewDonationTab from "./components/viewDonationTab";

export default function Donation() {
  const router = useRouter();
  const searchParams = useSearchParams();

  const [activeTab, setActiveTab] = useState("register");

  const handleTabChange = (key: React.Key) => {
    setActiveTab(key as string);
  };

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) router.push("/login");

    const tab = searchParams.get("tab");

    if (tab) setActiveTab(tab);
  }, []);

  return (
    <div className="p-2">
      <h1 className="text-3xl font-bold mb-5" id="donation-title">
        Donations
      </h1>
      <div className="w-full overflow-hidden p-4 mb-4 flex flex-col gap-4">
        <div className="flex-1 flex gap-2 min-h-0 rounded-2xl p-2">
          <div className="flex-1 flex flex-col p-4 overflow-auto">
            <Tabs
              id="donation-tabs"
              selectedKey={activeTab}
              onSelectionChange={handleTabChange}
            >
              <Tab key="register" id="register-tab" title="Register donation">
                <RegisterDonationTab />
              </Tab>

              <Tab key="update" id="update-tab" title="Update donation">
                <UpdateDonationTab />
              </Tab>

              <Tab key="view" id="view-tab" title="View donation">
                <ViewDonationTab />
              </Tab>
            </Tabs>
          </div>
        </div>
      </div>
    </div>
  );
}
