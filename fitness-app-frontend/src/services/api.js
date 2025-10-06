import axios from "axios";

const API_URL = "http://localhost:8080/api/";

const api = axios.create({
  baseURL: API_URL,
});

api.interceptors.request.use((config) => {
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("token");
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  if (userId) {
    config.headers["X-User-Id"] = userId;
  }
  return config;
});

export const getActivities = () => api.get("/activity");

export const addActivity = (activity) => api.post("/activity/ADDact", activity);

export const getActivityDetails = (id) => api.get(`/recs/activity/${id}`);

export const deleteRecAndActivity = (id) => api.delete(`/recs/delete/${id}`);
