import { Link } from "react-router-dom";
import { useEffect, useState } from "react";

export default function Navbar() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        // Проверяем наличие токена в localStorage
        const token = localStorage.getItem("token");
        setIsLoggedIn(!!token);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        setIsLoggedIn(false);
        window.location.href = "/"; // или navigate("/")
    };

    return (
        <nav className="navbar navbar-dark bg-dark px-4">
            <Link to="/home" className="navbar-brand text-decoration-none text-white">
                SplitApp
            </Link>

            <div>
                {isLoggedIn ? (
                    <button className="btn btn-outline-danger" onClick={handleLogout}>
                        Выйти
                    </button>
                ) : (
                    <>
                        <Link className="btn btn-outline-primary me-2" to="/login">
                            Вход
                        </Link>
                        <Link className="btn btn-outline-success" to="/register">
                            Регистрация
                        </Link>
                    </>
                )}
            </div>
        </nav>
    );
}
