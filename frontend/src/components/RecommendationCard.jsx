export default function RecommendationCard() {
  return (
    <div className="recommendation-card">

      <div className="d-flex justify-content-between align-items-center">

        <div>

          <span className="badge bg-success mb-2">
            ⭐ AI Recommended
          </span>

          <h4>Miyapur → GCET</h4>

          <p className="mb-1">
            <strong>Driver:</strong> Rahul Kumar
          </p>

          <p className="mb-1">
            <strong>Vehicle:</strong> Car
          </p>

          <p className="mb-1">
            <strong>Seats:</strong> 3 Available
          </p>

          <p className="mb-0">
            <strong>Fare:</strong> ₹90
          </p>

        </div>

        <div className="text-end">

          <h2 className="text-primary">
            98%
          </h2>

          <small className="text-muted">
            Route Match
          </small>

          <br />

          <button className="btn btn-primary mt-3">
            Book Ride
          </button>

        </div>

      </div>

    </div>
  );
}