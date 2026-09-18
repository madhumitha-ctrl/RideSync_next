import DashboardLayout from "../../components/DashboardLayout";
import ProfileCard from "../../components/ProfileCard";

export default function ProfilePage() {
  return (
    <DashboardLayout
      title="My Profile"
      subtitle="Manage your personal information."
    >
      <ProfileCard />

      <div className="card shadow-sm border-0 rounded-4 mt-4">
        <div className="card-body">

          <h4 className="fw-bold mb-3">
            Account Information
          </h4>

          <div className="row">

            <div className="col-md-4">
              <h6>Registered Since</h6>
              <p>January 2026</p>
            </div>

            <div className="col-md-4">
              <h6>Total Rides Posted</h6>
              <p>12</p>
            </div>

            <div className="col-md-4">
              <h6>Total Bookings</h6>
              <p>28</p>
            </div>

          </div>

        </div>
      </div>

    </DashboardLayout>
  );
}