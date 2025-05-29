import { addToast } from "@heroui/react";

export default function showSuccessToast(title: string, description?: string) {
  addToast({
    title,
    description,
    color: "success",
  });
}
