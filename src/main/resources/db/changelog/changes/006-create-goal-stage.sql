CREATE TABLE goal_stage (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    order_number INT NOT NULL,
    goal_id BIGINT NOT NULL,
    template_stage_id BIGINT NOT NULL,

    CONSTRAINT fk_goal_stage_goal
        FOREIGN KEY (goal_id)
        REFERENCES goal(id),

    CONSTRAINT fk_goal_stage_template_stage
        FOREIGN KEY (template_stage_id)
        REFERENCES template_stage(id)
);