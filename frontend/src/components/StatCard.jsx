export default function StatCard({
  title,
  value,
  icon,
  color
}) {
  return (
    <div className="card stat-card shadow-sm h-100">
      <div className="card-body d-flex justify-content-between align-items-center">

        <div>
          <h6 className="text-muted">{title}</h6>
          <h3 className="fw-bold">{value}</h3>
        </div>

        <div
          className="stat-icon"
          style={{ backgroundColor: color }}
        >
          <i className={`bi ${icon}`}></i>
        </div>

      </div>
    </div>
  );
}