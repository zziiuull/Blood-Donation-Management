import { Navbar } from "@/components/navbar";

export default function DonationLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <>
      <Navbar />
      <div className="min-h-full flex-grow flex flex-col">{children}</div>
    </>
  );
}
