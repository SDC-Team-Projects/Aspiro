CREATE TABLE template_stage (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    order_number INT NOT NULL,
    template_id BIGINT NOT NULL,

    CONSTRAINT fk_template_stage_template
        FOREIGN KEY (template_id)
        REFERENCES templates(id)
);