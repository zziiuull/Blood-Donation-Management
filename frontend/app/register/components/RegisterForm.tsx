"use client";

import { Button, Input, Select, SelectItem, Form } from "@heroui/react";
import { useState } from "react";
import { Icon } from "@iconify/react";
import { useRouter } from "next/navigation";

import states from "./states";

import FormContainer from "@/components/form/FormContainer";
import InputContainer from "@/components/form/InputContainer";
import axios from "@/services/axios";
import showFailToast from "@/services/toast/showFailToast";

export default function RegisterForm() {
  const router = useRouter();

  const [name, setName] = useState("");
  const [lastname, setLastname] = useState("");
  const [cpf, setCpf] = useState("");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");
  const [crm, setCrm] = useState("");
  const [state, setState] = useState(new Set([]));
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const togglePasswordVisibility = () =>
    setIsPasswordVisible(!isPasswordVisible);

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await axios.post(`/api/v1/register`, {
        name,
        lastname,
        email,
        password,
        cpf,
        phone,
        address,
        crmNumber: crm,
        crmState: state.values().next().value,
      });

      router.push("/donation");
    } catch (error) {
      showFailToast(error.response.data.message);
    }
  }

  return (
    <FormContainer>
      <h1 className="text-3xl text-center">Register</h1>
      <Form className="flex flex-col gap-5" onSubmit={handleSubmit}>
        <InputContainer>
          <Input
            isRequired
            label="Name"
            placeholder="Enter your name"
            size="lg"
            type="text"
            value={name}
            onValueChange={setName}
          />
          <Input
            isRequired
            label="Lastname"
            placeholder="Enter your lastname"
            size="lg"
            type="text"
            value={lastname}
            onValueChange={setLastname}
          />
        </InputContainer>
        <InputContainer>
          <Input
            isRequired
            label="Cpf"
            placeholder="Enter your cpf"
            size="lg"
            type="text"
            value={cpf}
            onValueChange={setCpf}
          />
          <Input
            isRequired
            label="Phone"
            placeholder="Enter your phone"
            size="lg"
            type="tel"
            value={phone}
            onValueChange={setPhone}
          />
          <Input
            isRequired
            label="Address"
            placeholder="Enter your address"
            size="lg"
            type="text"
            value={address}
            onValueChange={setAddress}
          />
        </InputContainer>
        <InputContainer>
          <Input
            isRequired
            label="Crm"
            placeholder="Enter your crm"
            size="lg"
            type="text"
            value={crm}
            onValueChange={setCrm}
          />
          <Select
            isRequired
            className="max-w-xs"
            label="State"
            placeholder="Select a state"
            selectedKeys={state}
            variant="bordered"
            onSelectionChange={setState}
          >
            {states.map((s) => (
              <SelectItem key={s.key}>{s.label}</SelectItem>
            ))}
          </Select>
        </InputContainer>
        <InputContainer>
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
            onValueChange={setPassword}
          />
        </InputContainer>
        <Button
          className="text-xl py-6 rounded-full"
          color="primary"
          type="submit"
        >
          Register
        </Button>
      </Form>
    </FormContainer>
  );
}
