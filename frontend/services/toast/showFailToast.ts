import { addToast } from "@heroui/react";

export default function showFailToast(title: string, description?: string) {
  addToast({
    title,
    description,
    color: "danger",
  });
}
