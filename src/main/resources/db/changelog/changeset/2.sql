CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


CREATE TABLE shared_bill (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    created_by INTEGER NOT NULL REFERENCES users(id)
);


CREATE TABLE shared_bill_users (
    shared_bill_id INTEGER NOT NULL REFERENCES shared_bill(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (shared_bill_id, user_id)
);


CREATE TABLE expense (
    id SERIAL PRIMARY KEY,
    shared_bill_id INTEGER NOT NULL REFERENCES shared_bill(id) ON DELETE CASCADE,
    description TEXT,
    amount NUMERIC(10, 2) NOT NULL,
    paid_by INTEGER NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW()
);


CREATE TABLE expense_share (
    id SERIAL PRIMARY KEY,
    expense_id INTEGER NOT NULL REFERENCES expense(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    share_amount NUMERIC(10, 2) NOT NULL
);


CREATE TABLE debt_payment (
    id SERIAL PRIMARY KEY,
    shared_bill_id INTEGER NOT NULL REFERENCES shared_bill(id) ON DELETE CASCADE,
    from_user INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    to_user INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    amount NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    paid_at TIMESTAMP DEFAULT NOW()
);



ALTER TABLE debt_payment
    ADD CONSTRAINT check_from_to_diff CHECK (from_user <> to_user);


