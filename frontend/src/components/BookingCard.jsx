export default function BookingCard({
  driver,
  route,
  date,
  time,
  fare,
  status,
}) {
  return (
    <div className="card shadow-sm border-0 rounded-4 mb-4">
      <div className="card-body">

        <div className="d-flex justify-content-between align-items-center mb-3">

          <div>
            <h4 className="fw-bold mb-1">{driver}</h4>
            <p className="text-muted mb-0">{route}</p>
          </div>

          <span
            className={`badge fs-6 ${
              status === "Upcoming"
                ? "bg-primary"
                : "bg-success"
            }`}
          >
            {status}
          </span>

        </div>

        <hr />

        <div className="row">

          <div className="col-md-4">
            <strong>📅 Date</strong>
            <p>{date}</p>
          </div>

          <div className="col-md-4">
            <strong>⏰ Time</strong>
            <p>{time}</p>
          </div>

          <div className="col-md-4">
            <strong>💰 Fare</strong>
            <p>₹{fare}</p>
          </div>

        </div>

        <div className="text-end">

          {status === "Upcoming" ? (
            <button className="btn btn-danger">
              Cancel Booking
            </button>
          ) : (
            <button className="btn btn-secondary" disabled>
              Completed
            </button>
          )}

        </div>

      </div>
    </div>
  );
}