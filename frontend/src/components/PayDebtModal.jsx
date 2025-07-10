import { useEffect, useState } from "react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";

export default function PayDebtModal({ onClose }) {
    const [accounts, setAccounts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        API.get("/shared-bills/with-debts").then((res) => {
            setAccounts(res.data);
        });
    }, []);

    return (
        <div>
            <h5>Выберите счёт для оплаты</h5>
            <ul className="list-group mt-3">
                {accounts.length === 0 ? (
                    <li className="list-group-item">Нет счетов с долгами</li>
                ) : (
                    accounts.map((acc) => (
                        <li
                            key={acc.id}
                            className="list-group-item list-group-item-action"
                            style={{ cursor: "pointer" }}
                            onClick={() => navigate(`/shared-bills/${acc.id}`)}
                        >
                            {acc.name}
                        </li>
                    ))
                )}
            </ul>
            <button className="btn btn-secondary mt-3" onClick={onClose}>Закрыть</button>
        </div>
    );
}
