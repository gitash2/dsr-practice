import API from "./axios";

export const register = (data) => API.post("/auth", data);

export const login = (form) =>
    API.post("/auth", form, {
        headers: {
            "Content-Type": "application/json",
        },
    });
