import { Megaphone } from "lucide-react";

export default function NoticeCard({
  title,
  description,
  date,
}) {
  return (
    <div className="notice-card">

      <div className="notice-icon">
        <Megaphone size={24} />
      </div>

      <div className="notice-content">

        <h5>{title}</h5>

        <p>{description}</p>

        <small>{date}</small>

      </div>

    </div>
  );
}