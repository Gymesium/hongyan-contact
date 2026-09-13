package cn.gymesium.directory.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "major")
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // SQLite 的自增主键必须是 INTEGER PRIMARY KEY，实体侧仍用 Long，仅调整 JDBC 绑定类型
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String code;

    @Column(nullable = false, unique = true, length = 64)
    private String name;

    @Column(nullable = false)
    private boolean enabled = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
