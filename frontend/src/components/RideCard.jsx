"use client";

import {
  MapPin,
  ArrowRight,
  Calendar,
  Clock,
  Car,
  Bike,
  Bus,
  Users,
  Star,
} from "lucide-react";
import { API_BASE, getStoredUser } from "../lib/api";

export default function RideCard({
  rideId,
  driver,
  pickup,
  destination,
  date,
  time,
  seats,
  vehicle,
  fare,
  rating,
  aiMatch,
}) {
    const VehicleIcon =
  vehicle.toLowerCase() === "car"
    ? Car
    : vehicle.toLowerCase() === "bike"
    ? Bike
    : vehicle.toLowerCase() === "scooter"
    ? Bike
    : vehicle.toLowerCase() === "bus"
    ? Bus
    : Car;

  const bookRide = async () => {
    const user = getStoredUser();
    if (!user?.userId) {
      alert("Please login before booking a ride.");
      return;
    }
    if (!rideId) {
      alert("This ride cannot be booked.");
      return;
    }

    const response = await fetch(`${API_BASE}/rides/${rideId}/book`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        passengerId: user.userId,
        seatsRequested: 1,
        pickupPoint: pickup,
      }),
    });

    const data = await response.json();
    if (!response.ok) {
      alert(data.message || "Booking failed");
      return;
    }
    alert(data.message || "Ride booked successfully");
  };

  return (
    <div className="ride-card card border-0 shadow-sm rounded-4 mb-4">
      <div className="card-body p-4">

        {/* Header */}
        <div className="d-flex justify-content-between align-items-start">

          <div>
            <h4 className="fw-bold mb-1">{driver}</h4>

            <div className="d-flex align-items-center text-warning">
              <Star size={18} fill="currentColor" />
              <span className="ms-2 fw-semibold">{rating} Rating</span>
            </div>
          </div>

          <span className="badge bg-success px-3 py-2 fs-6 rounded-pill">
            {aiMatch}% Match
          </span>

        </div>

        <hr className="my-4" />

        {/* Route */}
        <div className="route-section d-flex align-items-center justify-content-center mb-4">

          <div className="text-center">
            <MapPin size={22} className="text-primary mb-2" />
            <h6 className="fw-bold mb-0">{pickup}</h6>
            <small className="text-muted">Pickup</small>
          </div>

          <ArrowRight
            size={34}
            className="mx-5 text-primary"
          />

          <div className="text-center">
            <MapPin size={22} className="text-danger mb-2" />
            <h6 className="fw-bold mb-0">{destination}</h6>
            <small className="text-muted">Destination</small>
          </div>

        </div>

        {/* Details */}
        <div className="row text-center gy-3">

          <div className="col-md-3">
            <Calendar className="text-primary mb-2" size={22} />
            <div className="fw-semibold">{date}</div>
          </div>

          <div className="col-md-3">
            <Clock className="text-warning mb-2" size={22} />
            <div className="fw-semibold">{time}</div>
          </div>

          <div className="col-md-3">
            <VehicleIcon className="text-success mb-2" size={22} />
<div className="fw-semibold">{vehicle}</div>
    </div>      

          <div className="col-md-3">
            <Users className="text-info mb-2" size={22} />
            <div className="fw-semibold">{seats} Seats</div>
          </div>

        </div>

        <hr className="my-4" />

        {/* Footer */}
        <div className="d-flex justify-content-between align-items-center">

          <div>
            <h3 className="text-success fw-bold mb-0">
              ₹{fare}
            </h3>

            <small className="text-muted">
              per person
            </small>
          </div>

          <button type="button" className="btn btn-primary rounded-pill px-4 py-2" onClick={bookRide}>
            Book Ride →
          </button>

        </div>

      </div>
    </div>
  );
}
