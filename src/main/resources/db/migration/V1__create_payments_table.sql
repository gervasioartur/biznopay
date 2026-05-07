CREATE TABLE T_PAYMENTS
(
    id                  UUID         NOT NULL,
    idempotency_key     VARCHAR(255) NOT NULL,
    amount_in_cents     BIGINT       NOT NULL,
    currency            VARCHAR(3)   NOT NULL DEFAULT 'MZN',
    description         VARCHAR(255) NOT NULL,
    status              VARCHAR(20)  NOT NULL,
    type                VARCHAR(20)  NOT NULL,
    phone_number        VARCHAR(20),
    provider_payment_id VARCHAR(255),
    failure_reason      TEXT,
    retry_count         INTEGER      NOT NULL DEFAULT 0,
    created_at          TIMESTAMP    NOT NULL,
    updated_at          TIMESTAMP    NOT NULL,

    CONSTRAINT pk_payments PRIMARY KEY (id),
    CONSTRAINT uq_payments_idempotency_key UNIQUE (idempotency_key)
);

CREATE INDEX idx_payments_status
    ON t_payments (status);

CREATE INDEX idx_payments_idempotency_key
    ON t_payments (idempotency_key);

CREATE INDEX idx_payments_provider_payment_id
    ON t_payments (provider_payment_id);