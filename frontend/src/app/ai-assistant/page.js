"use client";

import { useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import ChatMessage from "../../components/ChatMessage";
import { API_BASE } from "../../lib/api";

export default function AIAssistantPage() {
  const [messages, setMessages] = useState([
    {
      sender: "ai",
      message:
        "Hi! 👋 I'm RideSync AI. I can help you find rides, estimate fares, and answer questions about the app.",
    },
  ]);

  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  const sendMessage = async () => {
    if (!input.trim() || loading) return;

    const userMessage = input.trim();

    setMessages((prev) => [
      ...prev,
      {
        sender: "user",
        message: userMessage,
      },
    ]);

    setInput("");
    setLoading(true);

    try {
      const response = await fetch(`${API_BASE}/ai/chat`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          message: userMessage,
        }),
      });

      const data = await response.json();

      setMessages((prev) => [
        ...prev,
        {
          sender: "ai",
          message:
            data.response || "Sorry, I could not generate a response.",
        },
      ]);
    } catch (error) {
      setMessages((prev) => [
        ...prev,
        {
          sender: "ai",
          message:
            "Unable to connect to the AI service. Please make sure the backend is running.",
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <DashboardLayout
      title="AI Assistant"
      subtitle="Get smart ride recommendations and instant help."
    >
      <div className="card shadow border-0 rounded-4">
        <div className="card-body p-4">

          <div
            style={{
              minHeight: "400px",
              maxHeight: "450px",
              overflowY: "auto",
            }}
          >
            {messages.map((msg, index) => (
              <ChatMessage
                key={index}
                sender={msg.sender}
                message={msg.message}
              />
            ))}

            {loading && (
              <ChatMessage
                sender="ai"
                message="Thinking..."
              />
            )}
          </div>

          <hr />

          <div className="input-group mt-3">
            <input
              type="text"
              className="form-control"
              placeholder="Ask RideSync AI..."
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
              disabled={loading}
            >
              {loading ? "..." : "Send"}
            </button>
          </div>

        </div>
      </div>
    </DashboardLayout>
  );
}