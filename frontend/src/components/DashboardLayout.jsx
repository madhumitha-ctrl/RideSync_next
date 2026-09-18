import Sidebar from "./Sidebar";
import Topbar from "./Topbar";

import "../styles/sidebar.css";
import "../styles/topbar.css";
import "../styles/dashboard.css";

export default function DashboardLayout({
  children,
  title,
  subtitle,
}) {
  return (
    <div className="dashboard-layout">

      <Sidebar />

      <div className="dashboard-content">

        <Topbar
          title={title}
          subtitle={subtitle}
        />

        {/* Main Page Content */}
        <div className="content">
          {children}
        </div>

      </div>

    </div>
  );
}