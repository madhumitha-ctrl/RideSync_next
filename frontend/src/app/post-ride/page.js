import DashboardLayout from "../../components/DashboardLayout";
import PostRideForm from "../../components/PostRideForm";

export default function PostRidePage() {
  return (
    <DashboardLayout
      title="Post Ride"
      subtitle="Share your ride with fellow students."
    >
      <PostRideForm />

      <div className="card shadow-sm border-0 rounded-4">
        <div className="card-body">

          <h4 className="fw-bold mb-3">
            💡 Ride Posting Tips
          </h4>

          <ul className="mb-0">
            <li>Provide the correct pickup and destination.</li>
            <li>Choose an accurate departure time.</li>
            <li>Set a reasonable fare per seat.</li>
            <li>Mention luggage or other special instructions in Notes.</li>
            <li>Update your ride if your plans change.</li>
          </ul>

        </div>
      </div>

    </DashboardLayout>
  );
}