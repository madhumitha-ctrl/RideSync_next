"use client";

import "../style.css";
import "../login.css";
import Link from "next/link";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { API_BASE, storeUser } from "../../lib/api";

export default function Login() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    const response = await fetch(`${API_BASE}/users/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    });

    const data = await response.json();
    if (!response.ok) {
      alert(data.message || "Login failed");
      return;
    }

    storeUser(data.user);
    router.push("/dashboard");
  };

  return (
    <div className="container main-container">

      {/* Image Section */}
      <div className="image-section">
        <img
          src="/images/car.png"
          alt="RideSync"
        />
      </div>

      {/* Login Form */}
      <div className="login-box">

        <h1>RideSync</h1>

        <h5>Smart Rides, Shared Journeys</h5>

        <form>

          <div className="mb-3">

            <label>Email</label>

            <input
              type="email"
              className="form-control"
              placeholder="Enter your email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

          </div>

          <div className="mb-3">

            <label>Password</label>

            <input
              type="password"
              className="form-control"
              placeholder="Enter your password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

          </div>

          <button
            type="button"
            className="btn btn-primary w-100"
            id="loginBtn"
            onClick={handleLogin}
          >
            Login
          </button>

        </form>

        <br />

        <div className="text-center">

          Don't have an account?{" "}

          <Link href="/register">
            Register
          </Link>

        </div>

      </div>

    </div>
  );
}
