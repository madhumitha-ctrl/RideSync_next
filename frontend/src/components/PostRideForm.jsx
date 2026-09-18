"use client";

import { useState } from "react";
import { API_BASE, getStoredUser } from "../lib/api";

export default function PostRideForm() {
  const [ride, setRide] = useState({
    pickup: "",
    destination: "",
    date: "",
    time: "",
    seats: 1,
    vehicleType: "Car",
    fare: 0,
    notes: "",
  });

  const handleChange = (e) => {
    setRide({
      ...ride,
      [e.target.name]: e.target.value,
    });
  };

  const postRide = async () => {
    const user = getStoredUser();

    if (!user?.userId) {
      alert("Please login before posting a ride.");
      return;
    }

    const payload = {
      driverId: user.userId,
      source: ride.pickup,
      destination: ride.destination,
      departureTime: `${ride.date}T${ride.time}:00`,
      totalSeats: Number(ride.seats),
      farePerSeat: Number(ride.fare),
      additionalNotes: ride.notes,
      vehicleType: String(ride.vehicleType).toUpperCase(),
    };

    const response = await fetch(`${API_BASE}/rides`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });

    const data = await response.json();

    if (!response.ok) {
      alert(data.message || "Failed to post ride");
      return;
    }

    alert("Ride Posted Successfully!");
  };

  return (
    <div className="card shadow border-0 rounded-4 mb-4">
      <div className="card-body p-4">

        <h3 className="fw-bold mb-4">➕ Post a Ride</h3>

        <div className="row g-3">

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Pickup Location
            </label>
            <input
              type="text"
              name="pickup"
              className="form-control"
              placeholder="Enter pickup location"
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Destination
            </label>
            <input
              type="text"
              name="destination"
              className="form-control"
              placeholder="Enter destination"
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Travel Date
            </label>
            <input
              type="date"
              name="date"
              className="form-control"
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Departure Time
            </label>
            <input
              type="time"
              name="time"
              className="form-control"
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Available Seats
            </label>
            <input
              type="number"
              name="seats"
              className="form-control"
              min="1"
              max="6"
              placeholder="Number of seats"
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Vehicle Type
            </label>
            <select
              name="vehicleType"
              className="form-select"
              value={ride.vehicleType}
              onChange={handleChange}
            >
              <option>Car</option>
              <option>Bike</option>
              <option>Scooter</option>
            </select>
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Fare per Seat (₹)
            </label>
            <input
              type="number"
              name="fare"
              className="form-control"
              placeholder="Enter fare"
              onChange={handleChange}
            />
          </div>

          <div className="col-12">
            <label className="form-label fw-semibold">
              Additional Notes
            </label>
            <textarea
              name="notes"
              className="form-control"
              rows="4"
              placeholder="Any special instructions..."
              onChange={handleChange}
            />
          </div>

          <div className="col-12 text-end mt-3">
            <button
              type="button"
              className="btn btn-primary px-4"
              onClick={postRide}
            >
              🚀 Post Ride
            </button>
          </div>

        </div>
      </div>
    </div>
  );
}