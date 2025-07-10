import { useState } from "react";

export default function AddUserToGroupPage({ onAddUser }) {
    const [username, setUsername] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        onAddUser(username);
        setUsername("");
    };

    return (
        <div className="container mt-4">
            <h2>Добавить пользователя в общий счёт</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Имя пользователя</label>
                    <input
                        type="text"
                        className="form-control"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary">
                    Добавить
                </button>
            </form>
        </div>
    );
}
