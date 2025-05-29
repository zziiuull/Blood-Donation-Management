"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

import RegisterForm from "./components/RegisterForm";

export default function RegisterPage() {
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (token) router.push("/donation");
  });

  return (
    <div className="flex flex-col items-center justify-center p-4 h-full">
      <RegisterForm />
      <Link className="mt-5 underline text-slate-500" href="/login">
        Login
      </Link>
    </div>
  );
}
