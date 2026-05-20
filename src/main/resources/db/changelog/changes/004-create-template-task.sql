CREATE TABLE template_task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    order_number INT NOT NULL,
    days_offset INT NOT NULL,
    duration_days INT NOT NULL,
    template_stage_id BIGINT NOT NULL,

    CONSTRAINT fk_template_task_template_stage
        FOREIGN KEY (template_stage_id)
        REFERENCES template_stage(id)
);