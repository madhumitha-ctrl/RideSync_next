export default function RideMessage({ sender, message }) {
  const isMe = sender === "me";

  return (
    <div
      className={`d-flex mb-3 ${
        isMe ? "justify-content-end" : "justify-content-start"
      }`}
    >
      <div
        className={`p-3 rounded-4 ${
          isMe
            ? "bg-primary text-white"
            : "bg-light"
        }`}
        style={{ maxWidth: "70%" }}
      >
        {message}
      </div>
    </div>
  );
}