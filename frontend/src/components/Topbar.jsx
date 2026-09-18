"use client";

import { Bell } from "lucide-react";

export default function Topbar({ title, subtitle }) {
  return (
    <div className="topbar">

      {/* Left Side */}
      <div>
        <h3 className="mb-0 fw-bold">{title}</h3>

        <small className="text-muted">
          {subtitle}
        </small>
      </div>

      {/* Right Side */}
      <div className="topbar-right">

        <button className="notification-btn">
          <Bell size={20} />
        </button>

        <div className="profile-box">
          <img
            src="https://i.pravatar.cc/100"
            alt="Profile"
          />

          <div>
            <h6 className="mb-0">
              Manish
            </h6>

            <small>
              Student
            </small>
          </div>
        </div>

      </div>

    </div>
  );
}