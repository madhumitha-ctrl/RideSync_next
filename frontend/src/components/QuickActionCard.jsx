import Link from "next/link";

export default function QuickActionCard({
  title,
  icon,
  href,
  color,
}) {
  return (
    <div className="col-lg-3 col-md-6">
      <Link href={href} className="text-decoration-none">
        <div className="quick-card">

          <div
            className="quick-icon"
            style={{ backgroundColor: color }}
          >
            <i className={`bi ${icon}`}></i>
          </div>

          <h5>{title}</h5>

        </div>
      </Link>
    </div>
  );
}