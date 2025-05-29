import {
  Table,
  TableHeader,
  TableColumn,
  TableBody,
  TableCell,
  TableRow,
} from "@heroui/react";
import React from "react";
import { Link } from "@heroui/react";

import { ExamStatus, SerologicalScreeningExam } from "@/types";
import { formatDateTime, examStatusMap } from "@/utils/utils";

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
  { name: "ACTION", uid: "action" },
];

interface SerologicalDetailsTableProps {
  exam: SerologicalScreeningExam | null | undefined;
  type: string;
}

export default function SerologicalDetailsTable({
  exam,
  type,
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
                {examStatusMap.get(exam.status)}
              </p>
            </div>
          );
        case "createdAt":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {formatDateTime(exam.createdAt ?? "") || "N/A"}
              </p>
            </div>
          );
        case "updatedAt":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {formatDateTime(exam.updatedAt ?? "") || "N/A"}
              </p>
            </div>
          );
        case "hepatitisB":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.hepatitisB ?? "N/A"}
              </p>
            </div>
          );
        case "hepatitisC":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.hepatitisC ?? "N/A"}
              </p>
            </div>
          );
        case "chagasDisease":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.chagasDisease ?? "N/A"}
              </p>
            </div>
          );
        case "syphilis":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.syphilis ?? "N/A"}
              </p>
            </div>
          );
        case "aids":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.aids ?? "N/A"}
              </p>
            </div>
          );
        case "htlv1_2":
          return (
            <div className="flex flex-col">
              <p className="text-bold text-sm capitalize text-default-400">
                {exam.htlv1_2 ?? "N/A"}
              </p>
            </div>
          );
        case "action":
          if (type === "view") {
            return (
              <Link
                className=""
                href={`/exams/serological-screening/${exam.donation?.id}/view`}
              >
                View
              </Link>
            );
          }

          return (
            <Link
              className=""
              href={`/exams/serological-screening/${exam.donation?.id}`}
              isDisabled={exam.status != ExamStatus.UNDER_ANALYSIS}
            >
              Update
            </Link>
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
      <TableBody emptyContent="No exam found" items={[exam]}>
        {(item) => (
          <TableRow key={item?.id}>
            {(columnKey) =>
              item ? (
                <TableCell>{renderCell(item, columnKey)}</TableCell>
              ) : (
                <TableCell>N/A</TableCell>
              )
            }
          </TableRow>
        )}
      </TableBody>
    </Table>
  );
}
