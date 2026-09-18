"use client";

import Link from "next/link";
import { CarFront } from "lucide-react";
import { usePathname } from "next/navigation";

import {
  LayoutDashboard,
  Search,
  Car,
  BookOpen,
  Bot,
  MessageCircle,
  Bell,
  User,
} from "lucide-react";

export default function Sidebar() {
  const pathname = usePathname();

  const menuItems = [
    { name: "Dashboard", href: "/dashboard", icon: LayoutDashboard },
    { name: "Search Ride", href: "/search-rides", icon: Search },
    { name: "Post Ride", href: "/post-ride", icon: Car },
    { name: "Bookings", href: "/booking-history", icon: BookOpen },
    { name: "AI Assistant", href: "/ai-assistant", icon: Bot },
    { name: "Ride Chat", href: "/ride-chat", icon: MessageCircle },
    { name: "Campus Notices", href: "/notices", icon: Bell },
    { name: "Profile", href: "/profile", icon: User },
  ];

  return (
    <div className="sidebar">
      <div className="logo">
    <CarFront size={32} />
    <span>RideSync</span>
    </div>
      <ul>
        {menuItems.map((item) => {
          const Icon = item.icon;

          return (
            <li key={item.href}>
              <Link
                href={item.href}
                className={pathname === item.href ? "active-link" : ""}
              >
                <Icon size={20} />
                <span>{item.name}</span>
              </Link>
            </li>
          );
        })}
      </ul>
    </div>
  );
}