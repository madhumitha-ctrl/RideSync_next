export const API_BASE = "http://localhost:8080";

export function getStoredUser() {
  if (typeof window === "undefined") {
    return null;
  }
  try {
    return JSON.parse(localStorage.getItem("ridesyncUser") || "null");
  } catch {
    return null;
  }
}

export function storeUser(user) {
  localStorage.setItem("ridesyncUser", JSON.stringify(user));
}

export function formatDateTimeParts(isoDateTime) {
  if (!isoDateTime) {
    return { date: "", time: "" };
  }
  const date = new Date(isoDateTime);
  return {
    date: date.toLocaleDateString(),
    time: date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
  };
}
