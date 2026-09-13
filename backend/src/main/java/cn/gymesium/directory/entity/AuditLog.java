package cn.gymesium.directory.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // SQLite 的自增主键必须是 INTEGER PRIMARY KEY，实体侧仍用 Long，仅调整 JDBC 绑定类型
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long id;

    @Column(nullable = false, length = 32)
    private String operator;

    @Column(nullable = false, length = 32)
    private String action;

    @Column(name = "target_username", length = 32)
    private String targetUsername;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public AuditLog() {
    }

    public AuditLog(String operator, String action, String targetUsername) {
        this.operator = operator;
        this.action = action;
        this.targetUsername = targetUsername;
    }

    public Long getId() { return id; }
    public String getOperator() { return operator; }
    public String getAction() { return action; }
    public String getTargetUsername() { return targetUsername; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
