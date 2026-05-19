CREATE TABLE goal (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,

    CONSTRAINT fk_goal_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_goal_template
        FOREIGN KEY (template_id)
        REFERENCES templates(id)
);