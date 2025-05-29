import Link from "next/link";

import RegisterForm from "./components/RegisterForm";

export default function RegisterPage() {
  return (
    <main className="flex flex-col items-center justify-center min-h-screen p-4">
      <RegisterForm />
      <Link className="mt-5 underline text-slate-500" href="/register">
        Login
      </Link>
    </main>
  );
}
