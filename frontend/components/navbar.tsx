"use client";

import {
  Navbar as HeroUINavbar,
  NavbarContent,
  NavbarBrand,
  NavbarItem,
  NavbarMenu,
  NavbarMenuItem,
  NavbarMenuToggle,
} from "@heroui/navbar";
import { Button } from "@heroui/react";
import { link as linkStyles } from "@heroui/theme";
import NextLink from "next/link";
import clsx from "clsx";
import { useRouter } from "next/navigation";
import { useState } from "react";

import { siteConfig } from "@/config/site";

export const Navbar = () => {
  const router = useRouter();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const handleLogout = () => {
    localStorage.removeItem("token");
    router.push("/login");
  };

  const handleMobileMenuClose = () => {
    setIsMenuOpen(false);
  };

  return (
    <HeroUINavbar
      id="navbar"
      isMenuOpen={isMenuOpen}
      maxWidth="full"
      position="sticky"
      onMenuOpenChange={setIsMenuOpen}
    >
      <NavbarContent className="sm:hidden" justify="start">
        <NavbarMenuToggle
          aria-label={isMenuOpen ? "Close menu" : "Open menu"}
          id="navbar-menu-toggle"
        />
      </NavbarContent>

      <NavbarContent className="basis-1/5 sm:basis-full" justify="end">
        <NavbarBrand className="gap-3 max-w-fit">
          <NextLink
            className="flex justify-start items-center gap-1"
            href="/"
            id="navbar-link-home"
          >
            <p className="font-bold text-inherit text-xl">BDM</p>
          </NextLink>
        </NavbarBrand>

        <div className="hidden lg:flex gap-6 ml-8">
          {siteConfig.navItems.map((item) => (
            <NavbarItem key={item.href}>
              <NextLink
                className={clsx(
                  linkStyles({ color: "foreground" }),
                  "data-[active=true]:text-primary data-[active=true]:font-medium",
                  "hover:text-primary transition-colors",
                )}
                href={item.href}
              >
                {item.label}
              </NextLink>
            </NavbarItem>
          ))}
        </div>

        <div className="hidden md:flex ml-auto">
          <Button
            color="danger"
            id="logout-desktop-button"
            onPress={handleLogout}
          >
            Logout
          </Button>
        </div>
      </NavbarContent>

      <NavbarMenu className="bg-background pt-6">
        <div className="flex flex-col gap-2">
          {siteConfig.navItems.map((item) => (
            <NavbarMenuItem key={item.href}>
              <NextLink
                className="w-full py-3 px-4 text-foreground hover:bg-default-100 rounded-lg block transition-colors"
                href={item.href}
                onClick={handleMobileMenuClose}
              >
                {item.label}
              </NextLink>
            </NavbarMenuItem>
          ))}
        </div>

        <NavbarMenuItem className="mt-6">
          <Button
            className="w-full"
            color="danger"
            id="logout-mobile-button"
            onPress={() => {
              handleLogout();
              handleMobileMenuClose();
            }}
          >
            Logout
          </Button>
        </NavbarMenuItem>
      </NavbarMenu>
    </HeroUINavbar>
  );
};
