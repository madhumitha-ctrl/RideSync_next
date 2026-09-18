import DashboardLayout from "../../components/DashboardLayout";
import ChatMessage from "../../components/ChatMessage";

export default function AIAssistantPage() {
  const messages = [
    {
      sender: "ai",
      message:
        "Hi! 👋 I'm RideSync AI. I can help you find rides, estimate fares, and answer questions about the app.",
    },
    {
      sender: "user",
      message: "Find rides from Ghatkesar to Uppal.",
    },
    {
      sender: "ai",
      message:
        "I found 3 rides matching your route. The best match departs at 8:30 AM with Rahul Reddy.",
    },
  ];

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
          </div>

          <hr />

          <div className="input-group mt-3">
            <input
              type="text"
              className="form-control"
              placeholder="Ask RideSync AI..."
            />

            <button className="btn btn-primary">
              Send
            </button>
          </div>

        </div>
      </div>
    </DashboardLayout>
  );
}