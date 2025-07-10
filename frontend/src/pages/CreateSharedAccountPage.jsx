import { useState, useEffect } from "react";
import API from "../api/axios.js";

export default function CreateSharedAccountPage({ onCreate }) {
    const [name, setName] = useState("");
    const [search, setSearch] = useState("");
    const [userResults, setUserResults] = useState([]);
    const [selectedUsers, setSelectedUsers] = useState([]);

    useEffect(() => {
        const delayDebounce = setTimeout(() => {
            if (search.trim().length >= 2) {
                API.get(`/users/search?username=${search}`).then((res) => {
                    setUserResults(res.data);
                });
            } else {
                setUserResults([]);
            }
        }, 300);

        return () => clearTimeout(delayDebounce);
    }, [search]);

    const addUser = (user) => {
        if (!selectedUsers.find((u) => u.id === user.id)) {
            setSelectedUsers([...selectedUsers, { ...user, amount: "" }]);
        }
        setSearch("");
        setUserResults([]);
    };

    const removeUser = (id) => {
        setSelectedUsers(selectedUsers.filter((u) => u.id !== id));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userIds = selectedUsers.map(u => u.id);

        try {
            const response = await API.post("/shared-bills", {
                name,
                users: userIds,
            });
            onCreate(response.data);
        } catch (err) {
            alert("Ошибка при создании счёта");
        }
    };


    return (
        <div>
            <h4>Создать общий счёт</h4>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label>Название счёта</label>
                    <input
                        className="form-control"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label>Добавить пользователей</label>
                    <input
                        className="form-control"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        placeholder="Введите имя пользователя..."
                    />
                    {userResults.length > 0 && (
                        <ul className="list-group mt-1">
                            {userResults.map((user) => (
                                <li
                                    key={user.id}
                                    className="list-group-item list-group-item-action"
                                    onClick={() => addUser(user)}
                                    style={{ cursor: "pointer" }}
                                >
                                    {user.username}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                <div className="mb-3">
                    <label>Выбранные пользователи</label>
                    <ul className="list-group">
                        {selectedUsers.map((user) => (
                            <li
                                key={user.id}
                                className="list-group-item d-flex align-items-center justify-content-between gap-2"
                            >
                                <span>{user.username}</span>
                                <button
                                    type="button"
                                    className="btn btn-sm btn-close ms-2"
                                    onClick={() => removeUser(user.id)}
                                    aria-label="Удалить"
                                />
                            </li>
                        ))}
                    </ul>
                </div>

                <button className="btn btn-primary" type="submit">
                    Создать
                </button>
            </form>
        </div>
    );
}
