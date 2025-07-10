import { useState } from "react";

export default function PayDebtPage({ onPay }) {
    const [amount, setAmount] = useState("");
    const [toUser, setToUser] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        onPay({ toUser, amount: parseFloat(amount) });
        setAmount("");
        setToUser("");
    };

    return (
        <div className="container mt-4">
            <h2>Оплатить долг</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Кому (username)</label>
                    <input
                        type="text"
                        className="form-control"
                        value={toUser}
                        onChange={(e) => setToUser(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Сумма</label>
                    <input
                        type="number"
                        className="form-control"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        min="0.01"
                        step="0.01"
                        required
                    />
                </div>
                <button type="submit" className="btn btn-warning">
                    Оплатить
                </button>
            </form>
        </div>
    );
}
