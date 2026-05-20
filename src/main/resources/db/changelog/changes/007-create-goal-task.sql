CREATE TABLE goal_task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    order_number INT NOT NULL,
    goal_stage_id BIGINT NOT NULL,
    template_task_id BIGINT NOT NULL,

    CONSTRAINT fk_goal_task_goal_stage
        FOREIGN KEY (goal_stage_id)
        REFERENCES goal_stage(id),

    CONSTRAINT fk_goal_task_template_task
        FOREIGN KEY (template_task_id)
        REFERENCES template_task(id)
);