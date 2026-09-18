"use client";

import { useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import SearchForm from "../../components/SearchForm";
import RideCard from "../../components/RideCard";
import { API_BASE, formatDateTimeParts } from "../../lib/api";

function mapRide(ride) {
  const { date, time } = formatDateTimeParts(ride.departureTime);
  return {
    rideId: ride.rideId,
    driver: ride.driverName,
    pickup: ride.source,
    destination: ride.destination,
    date,
    time,
    seats: ride.seatsAvailable,
    vehicle: ride.vehicleType ? String(ride.vehicleType).toLowerCase() : "car",
    fare: ride.farePerSeat,
    rating: ride.driverRating ?? 0,
    aiMatch: 90,
  };
}

export default function SearchRidePage() {
  const [rides, setRides] = useState([]);
  const [message, setMessage] = useState("Search to see available rides.");

  const handleSearch = async (filters) => {
    const params = new URLSearchParams();
    if (filters.pickup) {
      params.set("source", filters.pickup);
    }
    if (filters.destination) {
      params.set("destination", filters.destination);
    }
    if (filters.date) {
      params.set("date", filters.date);
    }

    const response = await fetch(`${API_BASE}/rides/search?${params.toString()}`);
    const data = await response.json();

    if (!response.ok) {
      setRides([]);
      setMessage(data.message || "Search failed");
      return;
    }

    let results = Array.isArray(data) ? data.map(mapRide) : [];

    if (filters.vehicleType && filters.vehicleType !== "All") {
      const wanted = filters.vehicleType.toLowerCase();
      results = results.filter((ride) => ride.vehicle === wanted);
    }
    if (filters.seats) {
      const needed = Number(filters.seats);
      results = results.filter((ride) => Number(ride.seats) >= needed);
    }

    setRides(results);
    setMessage(results.length ? "" : "No rides found.");
  };

  return (
   <DashboardLayout
  title="Search Ride"
  subtitle="Find rides that match your destination."
>

      <SearchForm onSearch={handleSearch} />

      <h3 className="fw-bold mb-4">
        Available Rides
      </h3>

      {message ? <p>{message}</p> : null}

      {rides.map((ride, index) => (
        <RideCard
          key={ride.rideId ?? index}
          {...ride}
        />
      ))}

    </DashboardLayout>
  );
}
