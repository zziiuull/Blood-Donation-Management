export default function InputContainer({
  children,
}: {
  children: React.ReactNode;
}) {
  return <div className="flex gap-3">{children}</div>;
}
