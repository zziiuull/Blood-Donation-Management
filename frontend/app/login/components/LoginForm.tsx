"use client";

import { Button, Input, Form } from "@heroui/react";
import { useState } from "react";
import { Icon } from "@iconify/react";

import FormContainer from "@/components/form/FormContainer";

export default function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const togglePasswordVisibility = () =>
    setIsPasswordVisible(!isPasswordVisible);

  async function handleSubmit(e) {
    e.preventDefault();
  }

  return (
    <FormContainer>
      <h1 className="text-3xl text-center">Login</h1>
      <Form className="flex flex-col gap-3" onSubmit={handleSubmit}>
        <Input
          isRequired
          label="Email"
          placeholder="Enter your email"
          size="lg"
          type="email"
          value={email}
          variant="bordered"
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
                <Icon
                  className="text-2xl text-default-400 pointer-events-none"
                  icon="lucide:eye"
                />
              ) : (
                <Icon
                  className="text-2xl text-default-400 pointer-events-none"
                  icon="lucide:eye-closed"
                />
              )}
            </button>
          }
          label="Password"
          placeholder="Enter your password"
          size="lg"
          type={isPasswordVisible ? "text" : "password"}
          value={password}
          variant="bordered"
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
    </FormContainer>
  );
}
