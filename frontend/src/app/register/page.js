"use client";

import "../style.css";
import "./register.css";
import Link from "next/link";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { API_BASE } from "../../lib/api";

export default function Register() {
  const router = useRouter();
  const [form, setForm] = useState({
    name: "",
    email: "",
    phone: "",
    gender: "",
    collegeId: "",
    password: "",
    confirmPassword: "",
  });

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleRegister = async () => {
    if (form.password !== form.confirmPassword) {
      alert("Passwords do not match");
      return;
    }

    const response = await fetch(`${API_BASE}/users/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: form.name,
        email: form.email,
        password: form.password,
        phone: form.phone,
        gender: form.gender ? form.gender.toUpperCase() : null,
        collegeId: form.collegeId,
      }),
    });

    const data = await response.json();
    if (!response.ok) {
      alert(data.message || "Registration failed");
      return;
    }

    alert(data.message || "Registration successful");
    router.push("/login");
  };

  return (
    <div className="container main-container">

      {/* Left Image */}
      <div className="image-section">
        <img
          src="/images/car.png"
          alt="RideSync"
        />
      </div>

      {/* Register Form */}
      <div className="login-box">

        <h1>RideSync</h1>

        <h5>Create Your Account</h5>

        <form>

          <div className="mb-2">
            <label>Name</label>
            <input
              type="text"
              name="name"
              className="form-control"
              placeholder="Enter your name"
              onChange={handleChange}
            />
          </div>

          <div className="mb-2">
            <label>College Email</label>
            <input
              type="email"
              name="email"
              className="form-control"
              placeholder="example@gcet.edu.in"
              onChange={handleChange}
            />
          </div>

          <div className="mb-2">
            <label>Phone Number</label>
            <input
              type="tel"
              name="phone"
              className="form-control"
              placeholder="Enter phone number"
              onChange={handleChange}
            />
          </div>

          <div className="mb-2">
            <label>Gender</label>
            <select name="gender" className="form-control" onChange={handleChange}>
              <option value="">Select Gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
          </div>

          <div className="mb-2">
            <label>College ID</label>
            <input
              type="text"
              name="collegeId"
              className="form-control"
              placeholder="23R11AXXXX"
              onChange={handleChange}
            />
          </div>

          <div className="mb-2">
            <label>Password</label>
            <input
              type="password"
              name="password"
              className="form-control"
              placeholder="Enter password"
              onChange={handleChange}
            />
          </div>

          <div className="mb-3">
            <label>Confirm Password</label>
            <input
              type="password"
              name="confirmPassword"
              className="form-control"
              placeholder="Confirm password"
              onChange={handleChange}
            />
          </div>

          <button
            type="button"
            className="btn btn-primary w-100"
            onClick={handleRegister}
          >
            Register
          </button>

        </form>

        <br />

        <div className="text-center">
          Already have an account?{" "}
          <Link href="/login">
            Login
          </Link>
        </div>

      </div>

    </div>
  );
}
