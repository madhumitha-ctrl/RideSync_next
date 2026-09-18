import DashboardLayout from "../../components/DashboardLayout";
import NoticeCard from "../../components/NoticeCard";

export default function NoticesPage() {

  const notices = [
    {
      title: "RideSync Launch",
      description:
        "RideSync is now available for all students. Start sharing rides safely and save travel costs.",
      date: "17 July 2026",
      category: "General",
    },
    {
      title: "Campus Placement Drive",
      description:
        "Software companies will visit the campus next week. Check the placement portal for eligibility.",
      date: "20 July 2026",
      category: "Placement",
    },
    {
      title: "Road Closure",
      description:
        "The main college gate road will remain closed tomorrow due to maintenance. Please use Gate 2.",
      date: "21 July 2026",
      category: "Transport",
    },
    {
      title: "New Pickup Point Added",
      description:
        "A new RideSync pickup point has been added near the Central Library.",
      date: "22 July 2026",
      category: "RideSync",
    },
  ];

  return (
    <DashboardLayout
      title="Campus Notices"
      subtitle="Stay updated with campus and RideSync announcements."
    >
      {notices.map((notice, index) => (
        <NoticeCard
          key={index}
          title={notice.title}
          description={notice.description}
          date={notice.date}
          category={notice.category}
        />
      ))}
    </DashboardLayout>
  );
}