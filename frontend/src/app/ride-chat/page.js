import DashboardLayout from "../../components/DashboardLayout";
import ChatUserCard from "../../components/ChatUserCard";
import RideMessage from "../../components/RideMessage";

export default function RideChatPage() {
  return (
    <DashboardLayout
      title="Ride Chat"
      subtitle="Chat with your ride partners."
    >
      <div className="card shadow border-0 rounded-4">
        <div className="row g-0">

          {/* Left Panel */}
          <div className="col-md-4 border-end">

            <ChatUserCard
              name="Rahul Reddy"
              lastMessage="See you at 8:30 AM."
              active
            />

            <ChatUserCard
              name="Priya Sharma"
              lastMessage="Ride confirmed."
            />

            <ChatUserCard
              name="Sai Kumar"
              lastMessage="Where are you?"
            />

          </div>

          {/* Right Panel */}
          <div className="col-md-8 p-4">

            <div
              style={{
                minHeight: "400px",
                maxHeight: "420px",
                overflowY: "auto",
              }}
            >
              <RideMessage
                sender="other"
                message="Hi! Are you coming?"
              />

              <RideMessage
                sender="me"
                message="Yes, I'll be there in 5 minutes."
              />

              <RideMessage
                sender="other"
                message="Great! I'm waiting near the college gate."
              />
            </div>

            <hr />

            <div className="input-group">
              <input
                type="text"
                className="form-control"
                placeholder="Type a message..."
              />

              <button className="btn btn-primary">
                Send
              </button>
            </div>

          </div>

        </div>
      </div>
    </DashboardLayout>
  );
}