export default function RecentRides() {
  return (
    <div className="card shadow-sm mt-4">
      <div className="card-body">

        <h4 className="mb-4">🕒 Recent Rides</h4>

        <table className="table align-middle">

          <thead>

            <tr>
              <th>Driver</th>
              <th>Route</th>
              <th>Fare</th>
              <th>Status</th>
            </tr>

          </thead>

          <tbody>

            <tr>
              <td>Rahul Kumar</td>
              <td>Miyapur → GCET</td>
              <td>₹90</td>
              <td>
                <span className="badge bg-success">
                  Completed
                </span>
              </td>
            </tr>

            <tr>
              <td>Priya Sharma</td>
              <td>KPHB → GCET</td>
              <td>₹50</td>
              <td>
                <span className="badge bg-warning text-dark">
                  Upcoming
                </span>
              </td>
            </tr>

            <tr>
              <td>Arjun</td>
              <td>Kukatpally → GCET</td>
              <td>₹70</td>
              <td>
                <span className="badge bg-primary">
                  Booked
                </span>
              </td>
            </tr>

          </tbody>

        </table>

      </div>
    </div>
  );
}