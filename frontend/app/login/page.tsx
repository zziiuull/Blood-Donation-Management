"use client";

import { Button, Input, Form } from "@heroui/react";
import { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const togglePasswordVisibility = () =>
    setIsPasswordVisible(!isPasswordVisible);

  async function handleSubmit(e) {
    e.preventDefault();
  }

  return (
    <main className="flex flex-col items-center justify-center min-h-screen p-4 gap-5">
      <h1 className="text-4xl">Blood Donation Management</h1>
      <h2 className="text-3xl">Login</h2>
      <Form className="flex flex-col gap-3" onSubmit={handleSubmit}>
        <Input
          isRequired
          label="Email"
          placeholder="Enter your email"
          size="lg"
          type="email"
          value={email}
          onValueChange={setEmail}
        />
        <Input
          isRequired
          endContent={
            <button
              aria-label="toggle password visibility"
              className="focus:outline-none"
              type="button"
              onClick={togglePasswordVisibility}
            >
              {isPasswordVisible ? (
                <FaEye className="text-2xl text-default-400 pointer-events-none" />
              ) : (
                <FaEyeSlash className="text-2xl text-default-400 pointer-events-none" />
              )}
            </button>
          }
          label="Password"
          placeholder="Enter your password"
          size="lg"
          type={isPasswordVisible ? "text" : "password"}
          value={password}
          onValueChange={setPassword}
        />
        <Button
          className="w-full text-xl py-6 rounded-full"
          color="primary"
          type="submit"
        >
          Login
        </Button>
      </Form>
    </main>
  );
}
