CREATE TYPE activity_level_enum AS ENUM(
    'SEDENTARY',          -- Little or no exercise
    'LIGHTLY_ACTIVE',     -- Light exercise 1–3 times per week
    'MODERATELY_ACTIVE',  -- Moderate exercise 3–5 times per week
    'VERY_ACTIVE',        -- Intense exercise 6–7 times per week
    'EXTRA_ACTIVE'        -- Very intense exercise daily
);

CREATE TABLE profiles(
    id             VARCHAR(255) NOT NULL,
    gender         VARCHAR(255) NOT NULL,
    birth_date     DATE NOT NULL,
    photo_url      VARCHAR(255),
    activity_level activity_level_enum NOT NULL,
    CONSTRAINT profiles_pk_id PRIMARY KEY (id)   -- Primary Key
);

CREATE TABLE goals(
    id                  VARCHAR(255) NOT NULL,
    goal_type           VARCHAR(255) NOT NULL,
    target_calories     INTEGER NOT NULL,
    target_weight       DECIMAL NOT NULL,
    protein_target      DECIMAL NOT NULL,
    carbs_target        DECIMAL NOT NULL,
    fat_target          DECIMAL NOT NULL,
    water_intake_target DECIMAL(5, 2) NOT NULL,
    deadline            DATE,
    CONSTRAINT goals_pk_id PRIMARY KEY (id)     -- Primary Key
);

CREATE TABLE users(
    id         VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    profile_id VARCHAR(255),
    goal_id    VARCHAR(255),
    CONSTRAINT users_pk_id          PRIMARY KEY (id),                                  -- Primary Key
    CONSTRAINT users_uq_email       UNIQUE      (email),                               -- Unique email
    CONSTRAINT users_uq_profile     UNIQUE      (profile_id),                          -- Unique Profile
    CONSTRAINT users_uq_goal        UNIQUE      (goal_id),                             -- Unique Goal
    CONSTRAINT users_fk_profile     FOREIGN KEY (profile_id) REFERENCES profiles (id), -- Foreign Key to Profiles
    CONSTRAINT users_fk_goal        FOREIGN KEY (goal_id)    REFERENCES goals    (id)  -- Foreign Key to Goals
);

CREATE INDEX users_idx_fk_profile ON users (profile_id);
CREATE INDEX users_idx_fk_goal    ON users (goal_id);

CREATE TABLE bodies(
    id        VARCHAR(255) NOT NULL,
    weight    DECIMAL(10, 2) NOT NULL,
    height    DECIMAL(5, 2) NOT NULL,
    date      DATE NOT NULL,
    user_id   VARCHAR(255) NOT NULL,
    CONSTRAINT bodies_pk_id         PRIMARY KEY (id),                           -- Primary Key
    CONSTRAINT bodies_uq_user_date  UNIQUE      (user_id, date),                -- Unique combination of User and date
    CONSTRAINT bodies_fk_user       FOREIGN KEY (user_id) REFERENCES users (id) -- Foreign Key to Users
);

CREATE INDEX bodies_idx_fk_user ON bodies (user_id);

CREATE TABLE nutritional_information(
    id                  VARCHAR(255) NOT NULL,
    calories            DECIMAL NOT NULL,
    carbohydrates       DECIMAL NOT NULL,
    protein             DECIMAL NOT NULL,
    fat                 DECIMAL NOT NULL,
    monounsaturated_fat DECIMAL NOT NULL,
    saturated_fat       DECIMAL NOT NULL,
    polyunsaturated_fat DECIMAL NOT NULL,
    trans_fat           DECIMAL NOT NULL,
    cholesterol         DECIMAL NOT NULL,
    sodium              DECIMAL NOT NULL,
    potassium           DECIMAL NOT NULL,
    fiber               DECIMAL NOT NULL,
    sugar               DECIMAL NOT NULL,
    calcium             DECIMAL NOT NULL,
    iron                DECIMAL NOT NULL,
    vitamin_a           DECIMAL NOT NULL,
    vitamin_c           DECIMAL NOT NULL,
    CONSTRAINT nutritional_information_pk_id PRIMARY KEY (id) -- Primary Key
);

CREATE TABLE foods(
    id                         VARCHAR(255) NOT NULL,
    title                      VARCHAR(255) NOT NULL,
    brand                      VARCHAR(255),
    description                VARCHAR(255),
    nutritional_information_id VARCHAR(255),
    user_id                    VARCHAR(255) NOT NULL,
    CONSTRAINT foods_pk_id                       PRIMARY KEY (id),                                                                 -- Primary Key
    CONSTRAINT foods_uq_nutritional_information  UNIQUE      (nutritional_information_id),                                         -- Unique Nutritional Information
    CONSTRAINT foods_fk_nutritional_information  FOREIGN KEY (nutritional_information_id) REFERENCES nutritional_information (id), -- Foreign Key to Nutritional Information
    CONSTRAINT foods_fk_user                     FOREIGN KEY (user_id)                    REFERENCES users (id)                    -- Foreign Key to Users
);

CREATE INDEX foods_idx_fk_nutritional_information ON foods (nutritional_information_id);
CREATE INDEX foods_idx_fk_user                    ON foods (user_id);

CREATE TABLE meals(
    id          VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    type        VARCHAR(255) NOT NULL,
    user_id     VARCHAR(255) NOT NULL,
    CONSTRAINT meals_pk_id   PRIMARY KEY (id),                           -- Primary Key
    CONSTRAINT meals_fk_user FOREIGN KEY (user_id) REFERENCES users (id) -- Foreign Key to Users
);

CREATE INDEX meals_idx_fk_user ON meals (user_id);

CREATE TABLE meal_logs(
    id            VARCHAR(255) NOT NULL,
    type          VARCHAR(255) NOT NULL,
    date          DATE NOT NULL,
    time          TIME WITHOUT TIME ZONE NOT NULL,
    calories_goal INTEGER NOT NULL,
    user_id       VARCHAR(255) NOT NULL,
    CONSTRAINT meal_logs_pk_id   PRIMARY KEY (id),                           -- Primary Key
    CONSTRAINT meal_logs_fk_user FOREIGN KEY (user_id) REFERENCES users (id) -- Foreign Key to Users
);

CREATE INDEX meal_logs_idx_fk_user ON meal_logs (user_id);

CREATE TABLE meal_log_preferences(
    id            VARCHAR(255) NOT NULL,
    type          VARCHAR(255) NOT NULL,
    time          TIME WITHOUT TIME ZONE NOT NULL,
    calories_goal INTEGER NOT NULL,
    user_id       VARCHAR(255) NOT NULL,
    CONSTRAINT meal_log_preferences_pk_id        PRIMARY KEY (id),                           -- Primary Key
    CONSTRAINT meal_log_preferences_uq_user_type UNIQUE      (user_id, type),                -- Unique combination of User and type
    CONSTRAINT meal_log_preferences_fk_user      FOREIGN KEY (user_id) REFERENCES users (id) -- Foreign Key to Users
);

CREATE INDEX meal_log_preferences_idx_fk_user ON meal_log_preferences (user_id);

CREATE TABLE meal_foods(
    food_id VARCHAR(255) NOT NULL,
    meal_id VARCHAR(255) NOT NULL,
    CONSTRAINT meal_foods_pk_meal_food PRIMARY KEY (food_id, meal_id),              -- Compound Key
    CONSTRAINT meal_foods_fk_food      FOREIGN KEY (food_id) REFERENCES foods (id), -- Foreign Key to Foods
    CONSTRAINT meal_foods_fk_meal      FOREIGN KEY (meal_id) REFERENCES meals (id)  -- Foreign Key to Meals
);

CREATE INDEX meal_foods_idx_fk_food ON meal_foods (food_id);
CREATE INDEX meal_foods_idx_fk_meal ON meal_foods (meal_id);

CREATE TABLE meal_log_foods(
    food_id     VARCHAR(255) NOT NULL,
    meal_log_id VARCHAR(255) NOT NULL,
    CONSTRAINT meal_log_foods_pk_meal_log_food PRIMARY KEY (food_id, meal_log_id),                 -- Compound Key
    CONSTRAINT meal_log_foods_fk_food          FOREIGN KEY (food_id)     REFERENCES foods (id),    -- Foreign Key to Foods
    CONSTRAINT meal_log_foods_fk_meal_log      FOREIGN KEY (meal_log_id) REFERENCES meal_logs (id) -- Foreign Key to Meal Logs
);

CREATE INDEX meal_log_foods_idx_fk_food     ON meal_log_foods (food_id);
CREATE INDEX meal_log_foods_idx_fk_meal_log ON meal_log_foods (meal_log_id);

CREATE TABLE meal_log_meals(
    meal_id     VARCHAR(255) NOT NULL,
    meal_log_id VARCHAR(255) NOT NULL,
    CONSTRAINT meal_log_meals_pk_meal_log_meals PRIMARY KEY (meal_id, meal_log_id),                 -- Compound Key
    CONSTRAINT meal_log_meals_fk_meal           FOREIGN KEY (meal_id)     REFERENCES meals (id),    -- Foreign Key to Meals
    CONSTRAINT meal_log_meals_fk_meal_log       FOREIGN KEY (meal_log_id) REFERENCES meal_logs (id) -- Foreign Key to Meal Logs
);

CREATE INDEX meal_log_meals_idx_fk_meal     ON meal_log_meals (meal_id);
CREATE INDEX meal_log_meals_idx_fk_meal_log ON meal_log_meals (meal_log_id);

CREATE TABLE roles(
    id        VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    CONSTRAINT roles_pk_id        PRIMARY KEY (id),   -- Primary Key
    CONSTRAINT roles_uq_authority UNIQUE (authority)  -- Unique authority
);

CREATE TABLE users_roles(
    role_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    CONSTRAINT users_roles_pk_role_user PRIMARY KEY (role_id, user_id),               -- Compound Key
    CONSTRAINT users_roles_fk_user      FOREIGN KEY (user_id) REFERENCES users (id),  -- Foreign Key to Users
    CONSTRAINT users_roles_fk_role      FOREIGN KEY (role_id) REFERENCES roles (id)   -- Foreign Key to Roles
);

CREATE INDEX users_roles_idx_fk_user ON users_roles (user_id);
CREATE INDEX users_roles_idx_fk_role ON users_roles (role_id);