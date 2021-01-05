CREATE TABLE client
(
    id   BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    age  INTEGER NOT NULL
);

CREATE TABLE account
(
    no  TEXT NOT NULL DEFAULT '' PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    rest DOUBLE PRECISION NOT NULL
);
