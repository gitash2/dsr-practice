import { useState } from "react";

export default function EditAccountForm({ account, onSave }) {
    const [newName, setNewName] = useState(account.name);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (newName.trim()) {
            onSave(newName.trim());
        }
    };

    return (
        <div>
            <h4>Редактировать счёт</h4>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Новое название</label>
                    <input
                        className="form-control"
                        value={newName}
                        onChange={(e) => setNewName(e.target.value)}
                        required
                    />
                </div>
                <button className="btn btn-primary" type="submit">
                    Сохранить
                </button>
            </form>
        </div>
    );
}
