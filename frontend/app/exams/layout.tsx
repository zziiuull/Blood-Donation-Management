import { Navbar } from "@/components/navbar";

export default function ExamsLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <>
      <Navbar />
      <div className="p-2">{children}</div>
    </>
  );
}
