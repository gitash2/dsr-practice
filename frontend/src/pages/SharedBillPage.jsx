import { useEffect, useState } from "react";
import API from "../api/axios";
import CreateExpenseForm from "../components/CreateExpenseForm";
import Modal from "../components/Modal";
import { useParams } from "react-router-dom";

export default function SharedBillPage() {
    const { id } = useParams();
    const [bill, setBill] = useState(null);
    const [expenses, setExpenses] = useState([]);
    const [debts, setDebts] = useState([]);
    const [showExpenseModal, setShowExpenseModal] = useState(false);
    const [currentUser, setCurrentUser] = useState(null);
    const [paymentAmounts, setPaymentAmounts] = useState({});

    useEffect(() => {
        fetchBill();
        fetchExpenses();
        fetchDebts();
        const stored = localStorage.getItem("user");
        if (stored) {
            setCurrentUser(JSON.parse(stored));
        }
    }, [id]);

    const fetchBill = async () => {
        const res = await API.get(`/shared-bills/${id}`);
        setBill(res.data);
    };

    const fetchExpenses = async () => {
        const res = await API.get(`/shared-bills/${id}/expenses`);
        setExpenses(Array.isArray(res.data) ? res.data : []);
    };

    const fetchDebts = async () => {
        const res = await API.get(`/shared-bills/${id}/debts`);
        setDebts(Array.isArray(res.data) ? res.data : []);
    };

    const handlePaymentChange = (key, value) => {
        setPaymentAmounts(prev => ({ ...prev, [key]: value }));
    };

    const handlePartialPayment = async (fromUser, toUser, fullAmount, i) => {
        const key = `${fromUser}-${toUser}`;
        const amount = parseFloat(paymentAmounts[key]);

        if (isNaN(amount) || amount <= 0 || amount > fullAmount) {
            alert("Введите корректную сумму");
            return;
        }

        try {
            await API.post(`/shared-bills/${id}/pay`, {
                sharedBillId: id,
                fromUser,
                toUser,
                amount
            });
            await fetchDebts();
            setPaymentAmounts(prev => ({ ...prev, [key]: "" }));
        } catch (err) {
            console.error("Ошибка при оплате:", err);
        }
    };

    return (
        <div className="container mt-4">
            <h2>Счёт: {bill?.name}</h2>

            <h5>Участники:</h5>
            <ul>
                {bill?.participants?.map(user => (
                    <li key={user.id}>{user.username}</li>
                ))}
            </ul>

            <div className="mt-4">
                <h5>Траты</h5>
                <button className="btn btn-primary mb-2" onClick={() => setShowExpenseModal(true)}>
                    Добавить трату
                </button>
                <ul className="list-group">
                    {expenses.map(exp => (
                        <li key={exp.id} className="list-group-item">
                            {exp.description}: {exp.amount} ₽ (платил {exp.payer})
                        </li>
                    ))}
                </ul>
            </div>

            <div className="mt-4">
                <h5>Задолженности</h5>
                <ul className="list-group">
                    {debts.map((debt, i) => {
                        const key = `${debt.fromUser}-${debt.toUser}`;
                        const isCurrentUserDebtor = currentUser?.username === debt.fromUser;

                        return (
                            <li key={i} className="list-group-item d-flex align-items-center justify-content-between">
                                <span>{debt.fromUser} должен {debt.toUser}: {debt.amount} ₽</span>
                                {isCurrentUserDebtor && (
                                    <div className="d-flex">
                                        <input
                                            className="form-control me-2"
                                            style={{ width: "100px" }}
                                            type="number"
                                            min="0.01"
                                            step="0.01"
                                            placeholder="Сумма"
                                            value={paymentAmounts[key] || ""}
                                            onChange={e => handlePaymentChange(key, e.target.value)}
                                        />
                                        <button
                                            className="btn btn-success"
                                            onClick={() => handlePartialPayment(debt.fromUser, debt.toUser, debt.amount, i)}
                                        >
                                            Оплатить
                                        </button>
                                    </div>
                                )}
                            </li>
                        );
                    })}
                </ul>
            </div>

            {showExpenseModal && (
                <Modal onClose={() => setShowExpenseModal(false)}>
                    <CreateExpenseForm id={id} onCreated={() => {
                        setShowExpenseModal(false);
                        fetchExpenses();
                        fetchDebts();
                    }} />
                </Modal>
            )}
        </div>
    );
}
