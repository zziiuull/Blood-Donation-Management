import Link from "next/link";

import RegisterForm from "./components/RegisterForm";

export default function RegisterPage() {
  return (
    <div className="flex flex-col items-center p-4">
      <RegisterForm />
      <Link className="mt-5 underline text-slate-500" href="/login">
        Login
      </Link>
    </div>
  );
}
