CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255),
    email VARCHAR(512),
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    owner_id     BIGINT                                  NOT NULL,
    name         VARCHAR(60)                             NOT NULL,
    description  VARCHAR(255)                            NOT NULL,
    is_available BOOLEAN                                 NOT NULL,
    request_id   BIGINT,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT FK_ITEM_OWNER FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    booker     BIGINT                                  NOT NULL,
    status     VARCHAR(255),
    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT FK_BOOKING_ITEM FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT FK_BOOKING_BOOKER FOREIGN KEY (booker) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    user_id    BIGINT                                  NOT NULL,
    text       VARCHAR(512)                            NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT FK_COMMENT_ITEM FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT FK_COMMENT_USER FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(512),
    author_id   BIGINT                                  NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT FK_REQUEST_REQUESTER FOREIGN KEY (author_id) REFERENCES users (id)
);