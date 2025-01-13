CREATE TABLE drink_logs(
    id                 VARCHAR(255)           NOT NULL,
    date               DATE                   NOT NULL,
    time               TIME WITHOUT TIME ZONE NOT NULL,
    liquid_intake      DECIMAL                NOT NULL,
    user_id            VARCHAR(255)           NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP              DEFAULT NOW(),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP              DEFAULT NOW(),
    CONSTRAINT drink_logs_pk_id   PRIMARY KEY (id),
    CONSTRAINT drink_logs_fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX drink_logs_idx_fk_user ON drink_logs (user_id);