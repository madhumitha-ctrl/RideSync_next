"use client";

import { useState } from "react";

export default function ProfileCard() {
  const [editing, setEditing] = useState(false);

  const [profile, setProfile] = useState({
    name: "Manish",
    collegeId: "GCET12345",
    email: "manish@gcet.edu.in",
    phone: "+91 9876543210",
    department: "Artificial Intelligence & Machine Learning",
    year: "4th Year",
  });

  const handleChange = (e) => {
    setProfile({
      ...profile,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="card shadow border-0 rounded-4">
      <div className="card-body p-4">

        <div className="text-center mb-4">

          <img
            src="/images/profile.jpg"
            alt="Profile"
            className="rounded-circle mb-3"
            width="120"
            height="120"
            style={{ objectFit: "cover" }}
          />

          <h3 className="fw-bold mb-1">
            {profile.name}
          </h3>

          <p className="text-muted">
            AIML Student
          </p>

        </div>

        <div className="row g-3">

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Full Name
            </label>
            <input
              className="form-control"
              name="name"
              value={profile.name}
              readOnly={!editing}
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              College ID
            </label>
            <input
              className="form-control"
              name="collegeId"
              value={profile.collegeId}
              readOnly={!editing}
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Email
            </label>
            <input
              className="form-control"
              name="email"
              value={profile.email}
              readOnly={!editing}
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Phone Number
            </label>
            <input
              className="form-control"
              name="phone"
              value={profile.phone}
              readOnly={!editing}
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Department
            </label>
            <input
              className="form-control"
              name="department"
              value={profile.department}
              readOnly={!editing}
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Year
            </label>
            <input
              className="form-control"
              name="year"
              value={profile.year}
              readOnly={!editing}
              onChange={handleChange}
            />
          </div>

        </div>

        <div className="text-end mt-4">

          <button
            className="btn btn-primary me-2"
            onClick={() => setEditing(!editing)}
          >
            {editing ? "Save Profile" : "Edit Profile"}
          </button>

          <button className="btn btn-outline-secondary">
            Change Password
          </button>

        </div>

      </div>
    </div>
  );
}