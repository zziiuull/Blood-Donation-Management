import Link from "next/link";

import LoginForm from "./components/LoginForm";

export default function LoginPage() {
  return (
    <div className="flex flex-col items-center mt-10 p-4">
      <LoginForm />
      <Link className="mt-5 underline text-slate-500" href="/register">
        Register account
      </Link>
    </div>
  );
}
