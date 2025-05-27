import {
  Table,
  TableHeader,
  TableColumn,
  TableBody,
  TableCell,
  TableRow,
} from "@heroui/react";
import React from "react";

import { ImmunohemalogyExam } from "@/types";
import { bloodTypeMap, formatDateTime } from "@/utils/utils";

export const columns = [
  { name: "STATUS", uid: "status" },
  { name: "CREATED AT", uid: "createdAt" },
  { name: "UPDATED AT", uid: "updatedAt" },
  { name: "BLOOD TYPE", uid: "bloodType" },
  { name: "IRREGULAR ANTIBODIES", uid: "irregularAntibodies" },
];

interface ImmunohematologyTableProps {
  exam: ImmunohemalogyExam;
}

export default function ImmunohematologyDetailsTable({
  exam,
}: ImmunohematologyTableProps) {
  const renderCell = React.useCallback(
    (exam: ImmunohemalogyExam, columnKey: React.Key) => {
      const cellValue = exam[columnKey as keyof ImmunohemalogyExam];

      switch (columnKey) {
        case "name":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.name}
              </p>
            </div>
          );
        case "status":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.status}
              </p>
            </div>
          );
        case "createdAt":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {formatDateTime(exam.createdAt)}
              </p>
            </div>
          );
        case "updatedAt":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {formatDateTime(exam.updatedAt)}
              </p>
            </div>
          );
        case "bloodType":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {bloodTypeMap.get(exam.bloodType)}
              </p>
            </div>
          );
        case "irregularAntibodies":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.irregularAntibodies}
              </p>
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
    },
    [],
  );

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
      <TableBody emptyContent="No exams found" items={[exam]}>
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
