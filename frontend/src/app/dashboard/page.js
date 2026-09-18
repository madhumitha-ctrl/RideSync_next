import DashboardLayout from "../../components/DashboardLayout";
import StatCard from "../../components/StatCard";
import QuickActionCard from "../../components/QuickActionCard";
import RecommendationCard from "../../components/RecommendationCard";
import RecentRides from "../../components/RecentRides";
import NoticeCard from "../../components/NoticeCard";

import "../../styles/cards.css";
import "../../styles/table.css";

export default function Dashboard() {
  return (
    <DashboardLayout
  title="Dashboard"
  subtitle="Welcome back, Manish 👋"
>
      {/* Statistics */}
      <div className="row g-4 mb-4">

        <div className="col-md-4">
          <StatCard
            title="Total Rides"
            value="24"
            icon="bi-car-front-fill"
            color="#4f46e5"
          />
        </div>

        <div className="col-md-4">
          <StatCard
            title="Active Bookings"
            value="6"
            icon="bi-calendar-check-fill"
            color="#0ea5e9"
          />
        </div>

        <div className="col-md-4">
          <StatCard
            title="Money Saved"
            value="₹1250"
            icon="bi-cash-stack"
            color="#22c55e"
          />
        </div>

      </div>

      {/* Quick Actions */}
      <h4 className="mb-3">Quick Actions</h4>

      <div className="row g-4 mb-5">

        <QuickActionCard
          title="Search Ride"
          href="/search-rides"
          icon="bi-search"
          color="#2563eb"
        />

        <QuickActionCard
          title="Post Ride"
          href="/post-ride"
          icon="bi-plus-circle-fill"
          color="#8b5cf6"
        />

        <QuickActionCard
          title="AI Assistant"
          href="/ai-assistant"
          icon="bi-robot"
          color="#10b981"
        />

        <QuickActionCard
          title="Ride Chat"
          href="/ride-chat"
          icon="bi-chat-dots-fill"
          color="#f59e0b"
        />

      </div>

      {/* AI Recommendation */}
      <RecommendationCard />

      {/* Recent Rides */}
      <RecentRides />

      {/* Campus Notices */}
      <div className="mt-4">

        <h4 className="mb-3">
          Campus Notices
        </h4>

        <NoticeCard
          title="Placement Drive"
          description="TCS placement drive starts tomorrow at 9:00 AM."
          date="Today"
        />

        <NoticeCard
          title="Hackathon"
          description="Registrations are now open for the college hackathon."
          date="Yesterday"
        />

      </div>

    </DashboardLayout>
  );
}