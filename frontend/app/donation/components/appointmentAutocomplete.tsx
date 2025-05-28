import { Autocomplete, AutocompleteItem, Chip } from "@heroui/react";
import { Icon } from "@iconify/react";
import { useEffect, useState } from "react";

import { formatDateTime } from "@/utils/utils";
import { Appointment } from "@/types";
import axios from "@/services/axios";

interface AppointmentAutocompleteProps {
  handleAppointmentSelect: (appointment: Appointment | null) => void;
}

const loadAppointments = async () => {
  try {
    const result = await axios.get<Appointment>("/api/v1/appointment", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    if (result.status === 200) return result.data;
  } catch (error) {
    return [];
  }
};

export const AppointmentAutocomplete = ({
  handleAppointmentSelect,
}: AppointmentAutocompleteProps) => {
  const [appointments, setAppointments] = useState<Appointment[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      const data = await loadAppointments();

      setAppointments(data);
    };

    fetchData();
  }, []);

  return (
    <Autocomplete
      aria-label="Select an appointment"
      classNames={{
        listboxWrapper: "max-h-[320px]",
        selectorButton: "text-default-500",
      }}
      defaultItems={appointments}
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
      placeholder="Select an appointment"
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
        const appointment = appointments.find((d) => d.id === id);

        if (appointment) {
          handleAppointmentSelect({
            ...appointment,
            status: appointment.status as Appointment["status"],
          });
        } else {
          handleAppointmentSelect(null);
        }
      }}
    >
      {(item) => (
        <AutocompleteItem
          key={item.id}
          textValue={`${formatDateTime(item.appointmentDate)} - ${item.collectionSite.name}`}
        >
          <div className="flex justify-between items-center">
            <div className="flex gap-2 items-center">
              <div className="flex flex-col">
                <span className="text-small font-semibold">
                  {formatDateTime(item.appointmentDate)}
                </span>
                <span className="text-tiny text-default-400">
                  {item.collectionSite.name}
                </span>
              </div>
            </div>
            <Chip
              className="ml-auto text-tiny font-bold"
              color={
                item.status === "SCHEDULED"
                  ? "warning"
                  : item.status === "CANCELED"
                    ? "danger"
                    : "success"
              }
              variant="dot"
            >
              {item.status}
            </Chip>
          </div>
        </AutocompleteItem>
      )}
    </Autocomplete>
  );
};
