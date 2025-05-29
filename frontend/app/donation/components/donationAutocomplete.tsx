import { Autocomplete, AutocompleteItem, Chip } from "@heroui/react";
import { Icon } from "@iconify/react";

import { useState, useEffect } from "react";

import { formatDateTime } from "@/utils/utils";
import { Donation, DonationStatus } from "@/types";
import { donationStatusMap } from "@/utils/utils";
import axios from "@/services/axios";

interface DonationAutocompleteProps {
  handleDonationSelect: (donation: Donation | null) => void;
}

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

export const DonationAutocomplete = ({
  handleDonationSelect,
}: DonationAutocompleteProps) => {

  const [donations, setDonations] = useState<Donation[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      const data = await loadDonations();

      setDonations(data ?? []);
    };

    fetchData();
  }, []);

  return (
    <Autocomplete
      aria-label="Select a donation"
      classNames={{
        listboxWrapper: "max-h-[320px]",
        selectorButton: "text-default-500",
      }}
      defaultItems={donations}
      inputProps={{
        classNames: {
          input: "ml-1",
          inputWrapper: "h-[48px]",
        },
      }}
      listboxProps={{
        hideSelectedIcon: true,
        itemClasses: {
          base: [
            "rounded-medium",
            "text-default-500",
            "transition-opacity",
            "data-[hover=true]:text-foreground",
            "dark:data-[hover=true]:bg-default-50",
            "data-[pressed=true]:opacity-70",
            "data-[hover=true]:bg-default-200",
            "data-[selectable=true]:focus:bg-default-100",
            "data-[focus-visible=true]:ring-default-500",
          ],
        },
      }}
      placeholder="Select a donation"
      popoverProps={{
        offset: 10,
        classNames: {
          base: "rounded-large",
          content: "p-1 border-small border-default-100 bg-background",
        },
      }}
      radius="lg"
      size="sm"
      startContent={<Icon className="text-default-400" icon="lucide:search" />}
      variant="flat"
      onSelectionChange={(id) => {
        const donation = donations.find((d) => d.id === id);

        handleDonationSelect(donation || null);
      }}
    >
      {(item) => (
        <AutocompleteItem key={item.id} textValue={item.donor?.name}>
          <div className="flex justify-between items-center">
            <div className="flex gap-2 items-center">
              <div className="flex flex-col">
                <span className="text-small font-semibold">
                  {item.donor?.name}
                </span>
                <span className="text-tiny text-default-400">
                  {`${formatDateTime(item.createdAt)} - ${item.appointment?.collectionSite.name}`}
                </span>
              </div>
            </div>
            <Chip
              className="ml-auto text-tiny font-bold"
              color={
                item.status === DonationStatus.UNDER_ANALYSIS
                  ? "warning"
                  : item.status === DonationStatus.REJECTED
                    ? "danger"
                    : "success"
              }
              variant="dot"
            >
              {item.status ? donationStatusMap.get(item.status) : ""}
            </Chip>
          </div>
        </AutocompleteItem>
      )}
    </Autocomplete>
  );
};
