-- 用例表索引
CREATE INDEX idx_test_case_owner ON test_cases(owner_id);
CREATE INDEX idx_test_case_parent ON test_cases(parent_id);

-- 实验记录表索引
CREATE INDEX idx_experiment_type ON experiment_records(type);
CREATE INDEX idx_experiment_status ON experiment_records(status);
CREATE INDEX idx_experiment_created ON experiment_records(created_at);

-- 探针记录表索引
CREATE INDEX idx_probe_target ON probe_records(target);
CREATE INDEX idx_probe_status ON probe_records(status); 