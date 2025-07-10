import {useState} from "react";
import {login} from "../api/auth";
import {useNavigate} from "react-router-dom";
import API from "../api/axios.js";

export default function LoginPage() {
    const [form, setForm] = useState({ username: "", password: "" });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });



    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await API.post("/auth/sign-in", form);
            console.log("res:", res);
            console.log("res.data:", res.data);


            localStorage.setItem("token", res.data.token);

            localStorage.setItem("user", JSON.stringify(res.data.user));

            navigate("/home");
        } catch (err) {
            setError("Неверный логин или пароль");
        }
    };

    return (
        <div className="auth-page d-flex justify-content-center align-items-center vh-100">
            <div className="auth-form col-md-6 col-lg-4">
                <h2 className="text-center mb-4">Вход</h2>
                {error && <div className="alert alert-danger">{error}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label>Логин</label>
                        <input
                            className="form-control"
                            name="username"
                            value={form.username}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label>Пароль</label>
                        <input
                            className="form-control"
                            type="password"
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <button className="btn btn-primary w-100">Войти</button>
                </form>
            </div>
        </div>
    );
}
