CREATE TYPE unit_enum AS ENUM (
    'GRAM',
    'MILLIGRAM',
    'KILOGRAM',
    'MICROGRAM',
    'MILLILITER',
    'LITER',
    'CALORIE',
    'KILOJOULE',
    'INTERNATIONAL_UNIT',
    'OUNCE',
    'CUP',
    'TABLESPOON',
    'TEASPOON',
    'SLICE',
    'PIECE',
    'BOWL'
);

CREATE TABLE portions(
    id          VARCHAR(255) NOT NULL,
    amount      INTEGER      NOT NULL,
    unit        unit_enum    NOT NULL,
    description TEXT,
    food_id     VARCHAR(255) NOT NULL,
    CONSTRAINT portions_pk_id   PRIMARY KEY (id),                                             -- Primary Key
    CONSTRAINT portions_uq_food UNIQUE      (food_id),                                        -- Unique Food
    CONSTRAINT portions_fk_food FOREIGN KEY (food_id) REFERENCES foods (id) ON DELETE CASCADE -- Foreign Key to Foods
);

CREATE INDEX portions_idx_fk_food on portions (food_id);