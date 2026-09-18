"use client";

import { useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";

export default function RideChatPage() {
  const [messages, setMessages] = useState([
    {
      sender: "Rahul Reddy",
      message: "Hi everyone! Is anyone traveling to Uppal tomorrow morning?",
      time: "8:30 AM",
    },
    {
      sender: "You",
      message: "Yes, I am traveling around 8:30 AM.",
      time: "8:32 AM",
    },
    {
      sender: "Priya Sharma",
      message: "Great! I can join the ride.",
      time: "8:35 AM",
    },
  ]);

  const [input, setInput] = useState("");

  const sendMessage = () => {
    if (!input.trim()) return;

    const newMessage = {
      sender: "You",
      message: input.trim(),
      time: new Date().toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      }),
    };

    setMessages((prev) => [...prev, newMessage]);
    setInput("");
  };

  return (
    <DashboardLayout
      title="Ride Chat"
      subtitle="Communicate with your ride members."
    >
      <div className="card shadow border-0 rounded-4">
        <div className="card-body p-4">

          {/* Chat Messages */}
          <div
            style={{
              minHeight: "400px",
              maxHeight: "450px",
              overflowY: "auto",
              padding: "10px",
            }}
          >
            {messages.map((msg, index) => (
              <div
                key={index}
                className={`d-flex mb-3 ${
                  msg.sender === "You"
                    ? "justify-content-end"
                    : "justify-content-start"
                }`}
              >
                <div
                  style={{
                    maxWidth: "70%",
                    padding: "12px 16px",
                    borderRadius: "15px",
                    background:
                      msg.sender === "You" ? "#2563eb" : "#f1f5f9",
                    color: msg.sender === "You" ? "white" : "#111827",
                  }}
                >
                  <div className="fw-bold mb-1">
                    {msg.sender}
                  </div>

                  <div>{msg.message}</div>

                  <small
                    style={{
                      opacity: 0.7,
                      display: "block",
                      marginTop: "5px",
                    }}
                  >
                    {msg.time}
                  </small>
                </div>
              </div>
            ))}
          </div>

          <hr />

          {/* Message Input */}
          <div className="input-group mt-3">
            <input
              type="text"
              className="form-control"
              placeholder="Type a message..."
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  sendMessage();
                }
              }}
            />

            <button
              className="btn btn-primary"
              onClick={sendMessage}
            >
              Send
            </button>
          </div>

        </div>
      </div>
    </DashboardLayout>
  );
}