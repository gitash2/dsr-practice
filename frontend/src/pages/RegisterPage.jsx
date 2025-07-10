import { useState } from "react";
import { register } from "../api/auth";
import { useNavigate } from "react-router-dom";

export default function RegisterPage() {
    const [form, setForm] = useState({ username: "", email: "", password: "" });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            await register(form);
            navigate("/");
        } catch (err) {
            setError("Ошибка регистрации");
        }
    };

    return (
        <div className="col-md-6 offset-md-3">
            <h2>Регистрация</h2>
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label>Имя пользователя</label>
                    <input className="form-control" name="username" value={form.username} onChange={handleChange} required />
                </div>
                <div className="mb-3">
                    <label>Пароль</label>
                    <input className="form-control" type="password" name="password" value={form.password} onChange={handleChange} required />
                </div>
                <button className="btn btn-primary">Зарегистрироваться</button>
            </form>
        </div>
    );
}
