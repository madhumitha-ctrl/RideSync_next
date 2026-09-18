"use client";

export default function ProfileCard() {
  return (
    <div className="card shadow border-0 rounded-4">
      <div className="card-body p-4">

        <div className="text-center mb-4">

          <img
            src="https://i.pravatar.cc/150"
            alt="Profile"
            className="rounded-circle mb-3"
            width="120"
            height="120"
          />

          <h3 className="fw-bold mb-1">
            Manish
          </h3>

          <p className="text-muted">
            AIML Student
          </p>

        </div>

        <div className="row g-3">

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Full Name
            </label>

            <input
              className="form-control"
              value="Manish"
              readOnly
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              College ID
            </label>

            <input
              className="form-control"
              value="GCET12345"
              readOnly
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Email
            </label>

            <input
              className="form-control"
              value="manish@gcet.edu.in"
              readOnly
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Phone Number
            </label>

            <input
              className="form-control"
              value="+91 9876543210"
              readOnly
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Department
            </label>

            <input
              className="form-control"
              value="Artificial Intelligence & Machine Learning"
              readOnly
            />
          </div>

          <div className="col-md-6">
            <label className="form-label fw-semibold">
              Year
            </label>

            <input
              className="form-control"
              value="4th Year"
              readOnly
            />
          </div>

        </div>

        <div className="text-end mt-4">

          <button className="btn btn-primary me-2">
            Edit Profile
          </button>

          <button className="btn btn-outline-secondary">
            Change Password
          </button>

        </div>

      </div>
    </div>
  );
}