import { Checkbox, cn } from "@heroui/react";

interface ExamCheckboxProps {
  name: string;
  description: string;
  isSelected: boolean;
  onChange: (checked: boolean) => void;
}

export const ExamCheckbox = ({
  name,
  description,
  isSelected,
  onChange,
}: ExamCheckboxProps) => {
  return (
    <Checkbox
      classNames={{
        base: cn(
          "w-full max-w-md bg-content1",
          "hover:bg-content2",
          "cursor-pointer rounded-lg gap-2",
          "data-[selected=true]:bg-content2",
        ),
        label: "w-full",
      }}
      isSelected={isSelected}
      onValueChange={onChange}
    >
      <div>
        <p>{name}</p>
        <span className="text-tiny text-default-500">{description}</span>
      </div>
    </Checkbox>
  );
};
