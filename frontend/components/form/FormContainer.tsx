export default function FormContainer({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="p-8 border rounded-lg flex flex-col gap-5 shadow-lg justify-center">
      {children}
    </div>
  );
}
