import { Autocomplete, AutocompleteItem, Divider } from "@heroui/react";
import { Icon } from "@iconify/react";
import { useState, useEffect } from "react";

import { bloodTypeMap } from "@/utils/utils";
import { Donor } from "@/types";
import axios from "@/services/axios";

interface DonorAutocompleteProps {
  handleDonorSelect: (donor: Donor | null) => void;
}

const loadDonors = async () => {
  try {
    const result = await axios.get<Donor[]>("/api/v1/donor", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    if (result.status === 200) return result.data;
  } catch (error) {
    return [];
  }
};

export const DonorAutocomplete = ({
  handleDonorSelect,
}: DonorAutocompleteProps) => {
  const [donors, setDonors] = useState<Donor[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      const data = await loadDonors();

      setDonors(data ?? []);
    };

    fetchData();
  }, []);

  return (
    <Autocomplete
      aria-label="Select a donor"
      classNames={{
        listboxWrapper: "max-h-[320px]",
        selectorButton: "text-default-500",
      }}
      defaultItems={donors}
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
      placeholder="Select a donor"
      popoverProps={{
        offset: 10,
        classNames: {
          base: "rounded-large",
          content: "p-1 border-small border-default-100 bg-background",
        },
      }}
      radius="lg"
      startContent={<Icon className="text-default-400" icon="lucide:search" />}
      variant="flat"
      onSelectionChange={(id) => {
        const donor = donors.find((d) => d.id === id);

        handleDonorSelect(donor ?? null);
      }}
    >
      {(item) => (
        <AutocompleteItem key={item.id} textValue={`${item.name}`}>
          <div className="flex justify-between items-center">
            <div className="flex gap-2 items-center">
              <div className="flex flex-col">
                <span className="text-small font-semibold">{item.name}</span>
                <span className="text-tiny text-default-500">
                  {item.contactInfo.email}
                </span>
                <div className="flex gap-2 h-4">
                  <span className="text-tiny text-default-500">{item.sex}</span>
                  <Divider orientation="vertical" />
                  <span className="text-tiny text-default-500">
                    {item.weight}kg
                  </span>
                  <Divider orientation="vertical" />
                  <span className="text-tiny text-default-500">
                    {bloodTypeMap.get(item.bloodType)}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </AutocompleteItem>
      )}
    </Autocomplete>
  );
};
