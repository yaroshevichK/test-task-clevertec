CREATE TABLE IF NOT EXISTS bank
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name varchar(100)
);

CREATE TABLE IF NOT EXISTS users
(
    id           SERIAL PRIMARY KEY NOT NULL,
    name         varchar(100),
    current_bank int
        constraint user_current_bank_fk
            references bank
);

CREATE TABLE IF NOT EXISTS currency
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name varchar(30)
);


CREATE TABLE IF NOT EXISTS account
(
    id      SERIAL PRIMARY KEY NOT NULL,
    number  varchar(10),
    currency_id int
    constraint account_currency_id_fk
    references currency,
    bank_id int
        constraint account_bank_id_fk
            references bank,
    user_id int
        constraint account_user_id_fk
            references users,
    date_open date,
    balance           double precision,
    date_income_percent date
);

CREATE TABLE IF NOT EXISTS transaction
(
    id               SERIAL PRIMARY KEY NOT NULL,
    date_transaction timestamp,
    type_transaction varchar(20),
    account_id       int
        constraint transactions_account_id_fk
            references account,
    account_transfer integer
    constraint transaction_account_transfer_fk
    references account,
    amount           double precision,
    note             varchar(150)
);
