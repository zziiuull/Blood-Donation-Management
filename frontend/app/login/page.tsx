"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

import LoginForm from "./components/LoginForm";

export default function LoginPage() {
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (token) router.push("/donation");
  });

  return (
    <div className="flex flex-col items-center mt-10 p-4">
      <LoginForm />
      <Link className="mt-5 underline text-slate-500" href="/register">
        Register account
      </Link>
    </div>
  );
}
