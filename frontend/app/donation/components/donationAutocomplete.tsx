import { Autocomplete, AutocompleteItem, Chip } from "@heroui/react";

import { donations } from "./donations";

import { formatDateTime } from "@/utils/utils";
import { SearchIcon } from "@/components/icons";
import { Donation, DonationStatus } from "@/types";

interface DonationAutocompleteProps {
  handleDonationSelect: (donation: Donation | null) => void;
}

const donationStatusMap: Map<DonationStatus, string> = new Map([
  ["APPROVED", "APPROVED"],
  ["REJECTED", "REJECTED"],
  ["UNDER_ANALYSIS", "UNDER ANALYSIS"],
]);

export const DonationAutocomplete = ({
  handleDonationSelect,
}: DonationAutocompleteProps) => {
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
      startContent={
        <SearchIcon className="text-default-400" size={20} strokeWidth={2.5} />
      }
      variant="flat"
      onSelectionChange={(id) => {
        const donation = donations.find((d) => d.id === id);

        handleDonationSelect(donation || null);
      }}
    >
      {(item) => (
        <AutocompleteItem key={item.id} textValue={item.donor.name}>
          <div className="flex justify-between items-center">
            <div className="flex gap-2 items-center">
              <div className="flex flex-col">
                <span className="text-small font-semibold">
                  {item.donor.name}
                </span>
                <span className="text-tiny text-default-400">
                  {`${formatDateTime(item.createdAt)} - ${item.appointment.collectionSite.name}`}
                </span>
              </div>
            </div>
            <Chip
              className="ml-auto text-tiny font-bold"
              color={
                item.donationStatus === DonationStatus.UNDER_ANALYSIS
                  ? "warning"
                  : item.donationStatus === DonationStatus.REJECTED
                    ? "danger"
                    : "success"
              }
              variant="dot"
            >
              {donationStatusMap.get(item.donationStatus)}
            </Chip>
          </div>
        </AutocompleteItem>
      )}
    </Autocomplete>
  );
};
