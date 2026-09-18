"use client";

import Link from "next/link";

export default function Navbar() {
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary shadow">
      <div className="container">

        <Link className="navbar-brand fw-bold" href="/dashboard">
          🚗 RideSync
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarNav">

          <ul className="navbar-nav ms-auto">

            <li className="nav-item">
              <Link className="nav-link" href="/dashboard">
                Dashboard
              </Link>
            </li>

            <li className="nav-item">
              <Link className="nav-link" href="/search-rides">
                Search Ride
              </Link>
            </li>

            <li className="nav-item">
              <Link className="nav-link" href="/post-ride">
                Post Ride
              </Link>
            </li>

            <li className="nav-item">
              <Link className="nav-link" href="/booking-history">
                Bookings
              </Link>
            </li>

            <li className="nav-item">
              <Link className="nav-link" href="/profile">
                Profile
              </Link>
            </li>

          </ul>

        </div>

      </div>
    </nav>
  );
}