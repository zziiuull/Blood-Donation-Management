"use client";

import { Button, Input, Select, SelectItem, Form } from "@heroui/react";
import { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";

import FormContainer from "./FormContainer";
import InputContainer from "./InputContainer";

export default function RegisterForm() {
  const [name, setName] = useState("");
  const [lastname, setLastname] = useState("");
  const [cpf, setCpf] = useState("");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");
  const [crm, setCrm] = useState("");
  const [state, setState] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const togglePasswordVisibility = () =>
    setIsPasswordVisible(!isPasswordVisible);

  const states = [
    { key: "AC", label: "Acre" },
    { key: "AL", label: "Alagoas" },
    { key: "AP", label: "Amapá" },
    { key: "AM", label: "Amazonas" },
    { key: "BA", label: "Bahia" },
    { key: "CE", label: "Ceará" },
    { key: "DF", label: "Distrito Federal" },
    { key: "ES", label: "Espírito Santo" },
    { key: "GO", label: "Goiás" },
    { key: "MA", label: "Maranhão" },
    { key: "MT", label: "Mato Grosso" },
    { key: "MS", label: "Mato Grosso do Sul" },
    { key: "MG", label: "Minas Gerais" },
    { key: "PA", label: "Pará" },
    { key: "PB", label: "Paraíba" },
    { key: "PR", label: "Paraná" },
    { key: "PE", label: "Pernambuco" },
    { key: "PI", label: "Piauí" },
    { key: "RJ", label: "Rio de Janeiro" },
    { key: "RN", label: "Rio Grande do Norte" },
    { key: "RS", label: "Rio Grande do Sul" },
    { key: "RO", label: "Rondônia" },
    { key: "RR", label: "Roraima" },
    { key: "SC", label: "Santa Catarina" },
    { key: "SP", label: "São Paulo" },
    { key: "SE", label: "Sergipe" },
    { key: "TO", label: "Tocantins" },
  ];

  async function handleSubmit(e) {
    e.preventDefault();
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
        </InputContainer>
      </Form>
      <Button
        className="text-xl py-6 rounded-full"
        color="primary"
        type="submit"
      >
        Register
      </Button>
    </FormContainer>
  );
}
