import {
  Table,
  TableHeader,
  TableColumn,
  TableBody,
  TableCell,
  TableRow,
} from "@heroui/react";
import React from "react";

import { SerologicalScreeningExam } from "@/types";
import { formatDateTime } from "@/utils/utils";

export const columns = [
  { name: "STATUS", uid: "status" },
  { name: "CREATED AT", uid: "createdAt" },
  { name: "UPDATED AT", uid: "updatedAt" },
  { name: "HEPATITIS B", uid: "hepatitisB" },
  { name: "HEPATITIS C", uid: "hepatitisC" },
  { name: "CHAGAS DISEASE", uid: "chagasDisease" },
  { name: "SYPHILIS", uid: "syphilis" },
  { name: "AIDS", uid: "aids" },
  { name: "HTLV I/II", uid: "htlv1_2" },
];

interface SerologicalDetailsTableProps {
  exam: SerologicalScreeningExam;
}

export default function SerologicalDetailsTable({
  exam,
}: SerologicalDetailsTableProps) {
  const renderCell = React.useCallback(
    (exam: SerologicalScreeningExam, columnKey: React.Key) => {
      const cellValue = exam[columnKey as keyof SerologicalScreeningExam];

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
        case "hepatitisB":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.hepatitisB}
              </p>
            </div>
          );
        case "hepatitisC":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.hepatitisC}
              </p>
            </div>
          );
        case "chagasDisease":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.chagasDisease}
              </p>
            </div>
          );
        case "syphilis":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.syphilis}
              </p>
            </div>
          );
        case "aids":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.aids}
              </p>
            </div>
          );
        case "htlv1_2":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.htlv1_2}
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
