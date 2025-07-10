import { useEffect, useState } from "react";
import CreateSharedAccountPage from "./CreateSharedAccountPage";
import PayDebtPage from "./PayDebtPage";
import Modal from "../components/Modal.jsx";
import EditAccountForm from "../components/EditAccountForm.jsx";
import API from "../api/axios";
import {Link} from "react-router-dom"; // axios instance с интерсептором

export default function HomePage() {
    const [showCreate, setShowCreate] = useState(false);
    const [showPay, setShowPay] = useState(false);
    const [accountToEdit, setAccountToEdit] = useState(null);
    const [accounts, setAccounts] = useState([]);

    useEffect(() => {
        fetchAccounts();
    }, []);

    const fetchAccounts = async () => {
        try {
            const response = await API.get("/shared-bills");
            setAccounts(response.data);
        } catch (error) {
            console.error("Ошибка при загрузке аккаунтов:", error);
        }
    };

    const handleCreateAccount = async (accountName, userIds) => {
        try {
            await API.post("/shared-bills", {
                name: accountName,
                userIds: userIds,
            });
            await fetchAccounts();
            setShowCreate(false);
        } catch (error) {
            console.error("Ошибка при создании счёта:", error);
        }
    };

    const handlePayDebt = ({ toUser, amount }) => {
        console.log("Оплачен долг:", toUser, amount);
        setShowPay(false);
    };

    const handleEditAccount = (account) => {
        setAccountToEdit(account);
    };

    return (
        <div className="container mt-5">
            <h2 className="mb-4 text-center">Главная</h2>

            <div className="d-flex justify-content-center gap-3 mb-4">
                <button className="btn btn-success" onClick={() => setShowCreate(true)}>
                    Создать общий счёт
                </button>
            </div>

            <h4 className="mb-3">Мои платёжные аккаунты:</h4>
            <div className="list-group mb-5">
                {accounts.length === 0 ? (
                    <div className="text-muted">Нет аккаунтов</div>
                ) : (
                    accounts.map((acc) => (
                        <div
                            key={acc.id}
                            className="list-group-item d-flex justify-content-between align-items-center"
                        >
                            <Link to={`/shared-bills/${acc.id}`} className="text-decoration-none text-dark">
                                {acc.name}
                            </Link>
                            <button
                                className="btn btn-outline-secondary btn-sm"
                                onClick={() => handleEditAccount(acc)}
                            >
                                Редактировать
                            </button>
                        </div>
                    ))
                )}
            </div>

            {showCreate && (
                <Modal onClose={() => setShowCreate(false)}>
                    <CreateSharedAccountPage onCreate={handleCreateAccount} />
                </Modal>
            )}

            {showPay && (
                <Modal onClose={() => setShowPay(false)}>
                    <PayDebtPage onPay={handlePayDebt} />
                </Modal>
            )}

            {accountToEdit && (
                <Modal onClose={() => setAccountToEdit(null)}>
                    <EditAccountForm
                        account={accountToEdit}
                        onSave={(newName) => {
                            setAccounts((prev) =>
                                prev.map((acc) =>
                                    acc.id === accountToEdit.id ? { ...acc, name: newName } : acc
                                )
                            );
                            setAccountToEdit(null);
                        }}
                    />
                </Modal>
            )}
        </div>
    );
}
