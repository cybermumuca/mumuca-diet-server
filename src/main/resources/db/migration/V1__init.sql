CREATE TABLE users(
    id         VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    CONSTRAINT users_pk_id    PRIMARY KEY (id),   -- Primary Key
    CONSTRAINT users_uq_email UNIQUE      (email) -- Unique email
);

CREATE TABLE roles(
    id        VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    CONSTRAINT roles_pk_id        PRIMARY KEY (id),   -- Primary Key
    CONSTRAINT roles_uq_authority UNIQUE (authority)  -- Unique authority
);

CREATE TABLE users_roles(
    user_id VARCHAR(255) NOT NULL,
    role_id VARCHAR(255) NOT NULL,
    CONSTRAINT users_roles_pk_user_role PRIMARY KEY (user_id, role_id),                                -- Compound Key
    CONSTRAINT users_roles_fk_user      FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE, -- Foreign Key to Users
    CONSTRAINT users_roles_fk_role      FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE  -- Foreign Key to Roles
);

CREATE INDEX users_roles_idx_fk_user ON users_roles (user_id);
CREATE INDEX users_roles_idx_fk_role ON users_roles (role_id);

CREATE TYPE gender_enum AS ENUM(
    'MALE',
    'FEMALE'
);

CREATE TYPE activity_level_enum AS ENUM(
    'SEDENTARY',          -- Little or no exercise
    'LIGHTLY_ACTIVE',     -- Light exercise 1–3 times per week
    'MODERATELY_ACTIVE',  -- Moderate exercise 3–5 times per week
    'VERY_ACTIVE',        -- Intense exercise 6–7 times per week
    'EXTRA_ACTIVE'        -- Very intense exercise daily
);

CREATE TABLE profiles(
    id             VARCHAR(255)        NOT NULL,
    gender         gender_enum         NOT NULL,
    birth_date     DATE                NOT NULL,
    photo_url      VARCHAR(255),
    activity_level activity_level_enum NOT NULL,
    user_id        VARCHAR(255)        NOT NULL,
    CONSTRAINT profiles_pk_id   PRIMARY KEY (id),                                             -- Primary Key
    CONSTRAINT profiles_uq_user UNIQUE      (user_id),                                        -- Unique User
    CONSTRAINT profiles_fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE -- Foreign Key to Users
);

CREATE INDEX profiles_idx_fk_user ON profiles (user_id);

CREATE TYPE goal_type_enum AS ENUM(
    'LOSE_WEIGHT',
    'GAIN_WEIGHT',
    'MAINTAIN_WEIGHT'
);

CREATE TABLE goals(
    id                  VARCHAR(255)   NOT NULL,
    goal_type           goal_type_enum NOT NULL,
    target_calories     INTEGER        NOT NULL,
    target_weight       DECIMAL        NOT NULL,
    protein_target      DECIMAL        NOT NULL,
    carbs_target        DECIMAL        NOT NULL,
    fat_target          DECIMAL        NOT NULL,
    water_intake_target DECIMAL        NOT NULL,
    deadline            DATE,
    user_id             VARCHAR(255)   NOT NULL,
    CONSTRAINT goals_pk_id   PRIMARY KEY (id),                                             -- Primary Key
    CONSTRAINT goals_uq_user UNIQUE      (user_id),                                        -- Unique User
    CONSTRAINT goals_fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE -- Foreign Key to Users
);

CREATE INDEX goals_idx_fk_user ON goals (user_id);

CREATE TABLE bodies(
    id        VARCHAR(255) NOT NULL,
    weight    DECIMAL      NOT NULL,
    height    DECIMAL      NOT NULL,
    date      DATE         NOT NULL,
    user_id   VARCHAR(255) NOT NULL,
    CONSTRAINT bodies_pk_id        PRIMARY KEY (id),                                             -- Primary Key
    CONSTRAINT bodies_uq_user_date UNIQUE      (user_id, date),                                  -- Unique combination of User and date
    CONSTRAINT bodies_fk_user      FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE -- Foreign Key to Users
);

CREATE INDEX bodies_idx_fk_user ON bodies (user_id);

CREATE TABLE foods(
    id                         VARCHAR(255) NOT NULL,
    title                      VARCHAR(255) NOT NULL,
    brand                      VARCHAR(255),
    description                VARCHAR(255),
    user_id                    VARCHAR(255) NOT NULL,
    CONSTRAINT foods_pk_id   PRIMARY KEY (id),                                             -- Primary Key
    CONSTRAINT foods_fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE -- Foreign Key to Users
);

CREATE INDEX foods_idx_fk_user ON foods (user_id);

CREATE TABLE nutritional_information(
    id                  VARCHAR(255) NOT NULL,
    calories            DECIMAL      NOT NULL DEFAULT 0,
    carbohydrates       DECIMAL      NOT NULL DEFAULT 0,
    protein             DECIMAL      NOT NULL DEFAULT 0,
    fat                 DECIMAL      NOT NULL DEFAULT 0,
    monounsaturated_fat DECIMAL      NOT NULL DEFAULT 0,
    saturated_fat       DECIMAL      NOT NULL DEFAULT 0,
    polyunsaturated_fat DECIMAL      NOT NULL DEFAULT 0,
    trans_fat           DECIMAL      NOT NULL DEFAULT 0,
    cholesterol         DECIMAL      NOT NULL DEFAULT 0,
    sodium              DECIMAL      NOT NULL DEFAULT 0,
    potassium           DECIMAL      NOT NULL DEFAULT 0,
    fiber               DECIMAL      NOT NULL DEFAULT 0,
    sugar               DECIMAL      NOT NULL DEFAULT 0,
    calcium             DECIMAL      NOT NULL DEFAULT 0,
    iron                DECIMAL      NOT NULL DEFAULT 0,
    vitamin_a           DECIMAL      NOT NULL DEFAULT 0,
    vitamin_c           DECIMAL      NOT NULL DEFAULT 0,
    food_id             VARCHAR(255) NOT NULL,
    CONSTRAINT nutritional_information_pk_id   PRIMARY KEY (id),                                             -- Primary Key
    CONSTRAINT nutritional_information_uq_food UNIQUE      (food_id),                                        -- Unique Food
    CONSTRAINT nutritional_information_fk_food FOREIGN KEY (food_id) REFERENCES foods (id) ON DELETE CASCADE -- Foreign Key to Foods
);

CREATE INDEX nutritional_information_idx_fk_food on nutritional_information (food_id);

CREATE TYPE meal_type_enum AS ENUM(
    'BREAKFAST',
    'BRUNCH',
    'LUNCH',
    'AFTERNOON_SNACK',
    'DINNER',
    'SUPPER',
    'SNACK',
    'PRE_WORKOUT',
    'POST_WORKOUT',
    'MIDNIGHT_SNACK'
);

CREATE TABLE meals(
    id          VARCHAR(255)   NOT NULL,
    title       VARCHAR(255)   NOT NULL,
    description VARCHAR(255),
    type        meal_type_enum NOT NULL,
    user_id     VARCHAR(255)   NOT NULL,
    CONSTRAINT meals_pk_id   PRIMARY KEY (id),                                             -- Primary Key
    CONSTRAINT meals_fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE -- Foreign Key to Users
);

CREATE INDEX meals_idx_fk_user ON meals (user_id);

CREATE TABLE meal_logs(
    id            VARCHAR(255)           NOT NULL,
    type          meal_type_enum         NOT NULL,
    date          DATE                   NOT NULL,
    time          TIME WITHOUT TIME ZONE NOT NULL,
    calories_goal INTEGER                NOT NULL,
    user_id       VARCHAR(255)           NOT NULL,
    CONSTRAINT meal_logs_pk_id   PRIMARY KEY (id),                                             -- Primary Key
    CONSTRAINT meal_logs_fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE -- Foreign Key to Users
);

CREATE INDEX meal_logs_idx_fk_user ON meal_logs (user_id);

CREATE TABLE meal_log_preferences(
    id            VARCHAR(255)           NOT NULL,
    type          VARCHAR(255)           NOT NULL,
    time          TIME WITHOUT TIME ZONE NOT NULL,
    calories_goal INTEGER                NOT NULL,
    user_id       VARCHAR(255)           NOT NULL,
    CONSTRAINT meal_log_preferences_pk_id        PRIMARY KEY (id),                                             -- Primary Key
    CONSTRAINT meal_log_preferences_uq_user_type UNIQUE      (user_id, type),                                  -- Unique combination of User and type
    CONSTRAINT meal_log_preferences_fk_user      FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE -- Foreign Key to Users
);

CREATE INDEX meal_log_preferences_idx_fk_user ON meal_log_preferences (user_id);

CREATE TABLE meals_foods(
    meal_id VARCHAR(255) NOT NULL,
    food_id VARCHAR(255) NOT NULL,
    CONSTRAINT meals_foods_pk_meal_food PRIMARY KEY (meal_id, food_id),                                -- Compound Key
    CONSTRAINT meals_foods_fk_meal      FOREIGN KEY (meal_id) REFERENCES meals (id) ON DELETE CASCADE, -- Foreign Key to Meals
    CONSTRAINT meals_foods_fk_food      FOREIGN KEY (food_id) REFERENCES foods (id) ON DELETE CASCADE  -- Foreign Key to Foods
);

CREATE INDEX meals_foods_idx_fk_meal ON meals_foods (meal_id);
CREATE INDEX meals_foods_idx_fk_food ON meals_foods (food_id);

CREATE TABLE meal_logs_foods(
    meal_log_id VARCHAR(255) NOT NULL,
    food_id     VARCHAR(255) NOT NULL,
    CONSTRAINT meal_logs_foods_pk_meal_log_food PRIMARY KEY (meal_log_id, food_id),                                    -- Compound Key
    CONSTRAINT meal_logs_foods_fk_meal_log      FOREIGN KEY (meal_log_id) REFERENCES meal_logs (id) ON DELETE CASCADE, -- Foreign Key to Meal Logs
    CONSTRAINT meal_logs_foods_fk_food          FOREIGN KEY (food_id)     REFERENCES foods (id)     ON DELETE CASCADE  -- Foreign Key to Foods
);

CREATE INDEX meal_logs_foods_idx_fk_meal_log ON meal_logs_foods (meal_log_id);
CREATE INDEX meal_logs_foods_idx_fk_food     ON meal_logs_foods (food_id);

CREATE TABLE meal_logs_meals(
    meal_log_id VARCHAR(255) NOT NULL,
    meal_id     VARCHAR(255) NOT NULL,
    CONSTRAINT meal_logs_meals_pk_meal_log_meal PRIMARY KEY (meal_log_id, meal_id),                                    -- Compound Key
    CONSTRAINT meal_logs_meals_fk_meal_log      FOREIGN KEY (meal_log_id) REFERENCES meal_logs (id) ON DELETE CASCADE, -- Foreign Key to Meal Logs
    CONSTRAINT meal_logs_meals_fk_meal          FOREIGN KEY (meal_id)     REFERENCES meals (id)     ON DELETE CASCADE  -- Foreign Key to Meals
);


CREATE INDEX meal_logs_meals_idx_fk_meal_log ON meal_logs_meals (meal_log_id);
CREATE INDEX meal_logs_meals_idx_fk_meal     ON meal_logs_meals (meal_id);