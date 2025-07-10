import { useEffect, useState } from "react";
import API from "../api/axios";

export default function CreateExpenseForm({ id, onCreated }) {
    const [description, setDescription] = useState("");
    const [amount, setAmount] = useState("");
    const [participants, setParticipants] = useState([]);
    const [selectedDebtors, setSelectedDebtors] = useState([]);

    const currentUser = JSON.parse(localStorage.getItem("user"));
    const currentUsername = currentUser?.username;

    useEffect(() => {
        API.get(`/shared-bills/${id}`).then(res => {
            const users = res.data.participants || [];
            setParticipants(users);
        });
    }, [id]);

    const toggleDebtor = (userId) => {
        setSelectedDebtors(prev =>
            prev.includes(userId)
                ? prev.filter(i => i !== userId)
                : [...prev, userId]
        );
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        await API.post(`/shared-bills/${id}/expense`, {
            description,
            amount: parseFloat(amount),
            debtors: selectedDebtors
        });

        onCreated();
    };

    return (
        <form onSubmit={handleSubmit}>
            <h5>Новая трата</h5>

            <div className="mb-2">
                <label>Описание</label>
                <input className="form-control" value={description} onChange={e => setDescription(e.target.value)} required />
            </div>

            <div className="mb-2">
                <label>Сумма</label>
                <input
                    className="form-control"
                    value={amount}
                    onChange={e => setAmount(e.target.value)}
                    type="text"
                    inputMode="decimal"
                    pattern="[0-9]*[.,]?[0-9]*"
                    required
                />
            </div>

            <div className="mb-2">
                <label>Кто должен</label>
                {participants
                    .filter(p => p.username !== currentUsername)
                    .map(p => (
                        <div key={p.id}>
                            <input
                                type="checkbox"
                                checked={selectedDebtors.includes(p.id)}
                                onChange={() => toggleDebtor(p.id)}
                            /> {p.username}
                        </div>
                    ))}
            </div>

            <button type="submit" className="btn btn-success">Добавить</button>
        </form>
    );
}
