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
    notes: ""
  });


  const handleChange = (e) => {

    setRide({
      ...ride,
      [e.target.name]: e.target.value
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
      vehicleType: String(ride.vehicleType).toUpperCase()
    };

    const response = await fetch(
      `${API_BASE}/rides`,
      {
        method: "POST",
        headers:{
          "Content-Type":"application/json"
        },
        body: JSON.stringify(payload)
      }
    );


    const data = await response.json();

    if (!response.ok) {
      alert(data.message || "Failed to post ride");
      return;
    }

    alert("Ride Posted Successfully!");

  };


  return (

<div>

<input
name="pickup"
placeholder="Pickup"
onChange={handleChange}
/>


<input
name="destination"
placeholder="Destination"
onChange={handleChange}
/>


<input
type="date"
name="date"
onChange={handleChange}
/>


<input
type="time"
name="time"
onChange={handleChange}
/>


<input
type="number"
name="seats"
onChange={handleChange}
/>


<select
name="vehicleType"
onChange={handleChange}
>

<option>Car</option>
<option>Bike</option>
<option>Scooter</option>

</select>


<input
name="fare"
type="number"
onChange={handleChange}
/>


<textarea
name="notes"
onChange={handleChange}
/>


<button
onClick={postRide}
>
🚀 Post Ride
</button>


</div>

  );

}
