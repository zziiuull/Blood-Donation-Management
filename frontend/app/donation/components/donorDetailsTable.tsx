import {
  Table,
  TableHeader,
  TableColumn,
  TableBody,
  TableCell,
  TableRow,
} from "@heroui/react";
import React from "react";

import { Donor } from "@/types";
import { formatDate } from "@/utils/utils";

export const columns = [
  { name: "NAME", uid: "name" },
  { name: "EMAIL", uid: "email" },
  { name: "CPF", uid: "cpf" },
  { name: "WEIGHT (kg)", uid: "weight" },
  { name: "BIRTH DATE", uid: "birthDate" },
  { name: "SEX", uid: "sex" },
];

interface DonorDetailsTableProps {
  donor: Donor;
}

export default function DonorDetailsTable({ donor }: DonorDetailsTableProps) {
  const renderCell = React.useCallback((donor: Donor, columnKey: React.Key) => {
    const cellValue = donor[columnKey as keyof Donor];

    switch (columnKey) {
      case "name":
        return (
          <div className="flex flex-col">
            <p className="text-bold text-sm text-default-400">{donor.name}</p>
          </div>
        );
      case "email":
        return (
          <div className="flex flex-col">
            <p className="text-bold text-sm text-default-400">
              {donor.contactInfo.email}
            </p>
          </div>
        );
      case "cpf":
        return (
          <div className="flex flex-col">
            <p className="text-bold text-sm text-default-400">
              {donor.cpf.number}
            </p>
          </div>
        );
      case "weight":
        return (
          <div className="flex flex-col">
            <p className="text-bold text-sm text-default-400">{donor.weight}</p>
          </div>
        );
      case "birthDate":
        return (
          <div className="flex flex-col">
            <p className="text-bold text-sm text-default-400">
              {formatDate(donor.birthDate)}
            </p>
          </div>
        );
      case "sex":
        return (
          <div className="flex flex-col">
            <p className="text-bold text-sm text-default-400">{donor.sex}</p>
          </div>
        );
      default:
        return (
          <div className="text-sm text-default-500">
            {typeof cellValue === "object"
              ? JSON.stringify(cellValue)
              : String(cellValue ?? "N/A")}
          </div>
        );
    }
  }, []);

  return (
    <Table aria-label="Example table with custom cells">
      <TableHeader columns={columns}>
        {(column) => (
          <TableColumn
            key={column.uid}
            align={column.uid === "actions" ? "center" : "start"}
          >
            {column.name}
          </TableColumn>
        )}
      </TableHeader>
      <TableBody emptyContent="No donors found" items={[donor]}>
        {(item) => (
          <TableRow key={item.id}>
            {(columnKey) => (
              <TableCell>{renderCell(item, columnKey)}</TableCell>
            )}
          </TableRow>
        )}
      </TableBody>
    </Table>
  );
}
