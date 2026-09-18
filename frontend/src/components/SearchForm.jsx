"use client";

import { useState } from "react";

export default function SearchForm({ onSearch }) {
  const [filters, setFilters] = useState({
    pickup: "",
    destination: "",
    date: "",
    time: "",
    seats: "",
    vehicleType: "All",
  });

  const handleChange = (e) => {
    setFilters({
      ...filters,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (onSearch) {
      onSearch(filters);
    }
  };

  return (
    <div className="card shadow border-0 rounded-4 mb-4">
      <div className="card-body p-4">

        <h3 className="fw-bold mb-4">
          🔍 Search Ride
        </h3>

        <form onSubmit={handleSubmit}>
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
              Date
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
              Time
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
              Seats Required
            </label>

            <input
              type="number"
              name="seats"
              className="form-control"
              min="1"
              placeholder="Seats"
              onChange={handleChange}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Vehicle Type
            </label>

            <select name="vehicleType" className="form-select" onChange={handleChange}>
              <option>All</option>
              <option>Car</option>
              <option>Bike</option>
              <option>Scooter</option>
            </select>
          </div>

          <div className="col-12 text-end mt-3">
            <button type="submit" className="btn btn-primary px-4">
              Search Ride
            </button>
          </div>

        </div>
        </form>

      </div>
    </div>
  );
}
