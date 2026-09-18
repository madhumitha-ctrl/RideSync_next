export default function ChatMessage({
  sender,
  message,
}) {
  const isUser = sender === "user";

  return (
    <div
      className={`d-flex mb-3 ${
        isUser ? "justify-content-end" : "justify-content-start"
      }`}
    >
      <div
        className={`p-3 rounded-4 shadow-sm ${
          isUser
            ? "bg-primary text-white"
            : "bg-light"
        }`}
        style={{
          maxWidth: "70%",
        }}
      >
        <strong>
          {isUser ? "You" : "RideSync AI"}
        </strong>

        <div className="mt-1">
          {message}
        </div>
      </div>
    </div>
  );
}