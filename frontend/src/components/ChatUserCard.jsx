export default function ChatUserCard({ name, lastMessage, active }) {
  return (
    <div
      className={`d-flex align-items-center p-3 border-bottom ${
        active ? "bg-light" : ""
      }`}
      style={{ cursor: "pointer" }}
    >
      <img
        src="https://i.pravatar.cc/50"
        alt="User"
        className="rounded-circle"
        width="50"
        height="50"
      />

      <div className="ms-3">
        <h6 className="mb-1">{name}</h6>
        <small className="text-muted">{lastMessage}</small>
      </div>
    </div>
  );
}