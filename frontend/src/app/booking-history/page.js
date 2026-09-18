"use client";

import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import BookingCard from "../../components/BookingCard";
import { API_BASE, formatDateTimeParts, getStoredUser } from "../../lib/api";

function mapBooking(booking) {
  const { date, time } = formatDateTimeParts(booking.departureTime);
  const status = booking.status === "ACCEPTED" || booking.status === "PENDING"
    ? "Upcoming"
    : "Completed";
  return {
    driver: booking.driverName,
    route: `${booking.source} → ${booking.destination}`,
    date,
    time,
    fare: booking.fare,
    status,
  };
}

export default function BookingHistoryPage() {
  const [bookings, setBookings] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const user = getStoredUser();
    if (!user?.userId) {
      setMessage("Please login to view bookings.");
      return;
    }

    fetch(`${API_BASE}/users/${user.userId}/bookings`)
      .then(async (response) => {
        const data = await response.json();
        if (!response.ok) {
          setMessage(data.message || "Failed to load bookings");
          return;
        }
        setBookings(Array.isArray(data) ? data.map(mapBooking) : []);
      })
      .catch(() => setMessage("Failed to load bookings"));
  }, []);

  return (
    <DashboardLayout
      title="Booking History"
      subtitle="View your current and previous ride bookings."
    >

      <div className="mb-4">
        <h3 className="fw-bold">
          My Bookings
        </h3>
      </div>

      {message ? <p>{message}</p> : null}

      {bookings.map((booking, index) => (
        <BookingCard
          key={index}
          {...booking}
        />
      ))}

    </DashboardLayout>
  );
}
