ALTER TABLE user_contribution_log ADD project_id BIGINT REFERENCES projects(project_id);