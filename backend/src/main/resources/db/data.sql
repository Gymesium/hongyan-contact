-- 预置专业（每次启动执行，幂等：依赖 code/name 唯一约束 + insert or ignore）
-- 数据来自「系统补充信息」页面的专业列表（SQLite：布尔用 1/0 表示）
insert or ignore into major (code, name, enabled) values
    -- 数学与计算机学院
    ('CS',  '计算机科学与技术', 1),
    ('DS',  '数据科学与大数据技术', 1),
    ('AM',  '应用数学', 1),
    ('FM',  '金融数学', 1),
    -- 生命科学学院
    ('BIO', '生物科学', 1),
    ('BT',  '生物技术', 1),
    ('ECO', '生态学', 1),
    ('AQ',  '水产养殖', 1);
