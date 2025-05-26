export default function DonationLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return <div className="min-h-full flex-grow flex flex-col">{children}</div>;
}
