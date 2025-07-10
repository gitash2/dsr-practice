import axios from "axios";

const API = axios.create({
    baseURL: "http://localhost:8080",
});


API.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");

    const publicPaths = ["/auth/sign-in", "/auth"];

    const isPublic = publicPaths.some((path) => config.url?.includes(path));

    if (token && !isPublic) {
        config.headers.Authorization = `Bearer ${token.trim()}`;
    }
    return config;
});

export default API;
