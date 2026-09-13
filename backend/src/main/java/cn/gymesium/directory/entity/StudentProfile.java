package cn.gymesium.directory.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "student_profile")
public class StudentProfile {

    @Id
    @Column(name = "account_id")
    private Long accountId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "class_name", length = 32)
    private String className;

    @Column(name = "enroll_year")
    private Integer enrollYear;

    @Column(name = "graduate_year")
    private Integer graduateYear;

    @Column(length = 64)
    private String employer;

    @Column(length = 32)
    private String city;

    @Column(length = 32)
    private String phone;

    @Column(length = 64)
    private String email;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public Major getMajor() { return major; }
    public void setMajor(Major major) { this.major = major; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Integer getEnrollYear() { return enrollYear; }
    public void setEnrollYear(Integer enrollYear) { this.enrollYear = enrollYear; }
    public Integer getGraduateYear() { return graduateYear; }
    public void setGraduateYear(Integer graduateYear) { this.graduateYear = graduateYear; }
    public String getEmployer() { return employer; }
    public void setEmployer(String employer) { this.employer = employer; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
