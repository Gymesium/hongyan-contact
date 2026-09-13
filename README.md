# 鸿雁通讯录（HongYan Contacts）

> 「鸿雁传书」——为学院历届各专业学生提供的一个**注册审核制**校友网上通讯录系统。
> WEB 程序设计课程大作业（`hongyan-contacts`）。

## 功能简介

| 角色 | 功能 |
| --- | --- |
| 学生 | 注册（学号 + 姓名 + 密码）→ 管理员审核通过后登录 → 完善/修改本人通讯录 → 检索同学通讯录 |
| 管理员 | 账户审核（通过/驳回）、删除未审核账户、禁用/启用已审核账户、专业字典维护（增改/停用）；**无权修改学生通讯录** |
| 系统 | 记录最近登录时间与登录次数、管理员操作留痕（audit_log）、XSS 转义 + 参数化查询双重安全防护 |

通讯录字段：专业、班级、入校年份、毕业年份、就业单位、所在城市、联系方式、电子邮箱。

账号状态机：`PENDING`（待审核）→ `APPROVED`（可登录）/ `REJECTED`（可删除）；`APPROVED` 可被 `DISABLED`（禁用）后再启用。

## 技术栈

- **后端**：JDK 25 · Spring Boot 4.1.1（Web / Data JPA / Validation / Security / Actuator）· JJWT 0.12.6 · SQLite（xerial JDBC + Hibernate 社区方言，Spring SQL 初始化建表）· Gradle（Kotlin DSL）
- **前端**：Vue 3 · Vite 7 · Vue Router 4 · Pinia 3 · Axios（原生 CSS）
- **部署**：Docker Compose（后端容器 + SQLite 数据卷）+ Nginx + HTTPS；前端 Cloudflare Pages

## 目录结构

```
hongyan-contacts/
├── backend/                     # Spring Boot 后端（Gradle）
│   ├── build.gradle.kts
│   ├── Dockerfile
│   └── src/main/
│       ├── java/cn/gymesium/directory/
│       │   ├── BackendApplication.java
│       │   ├── config/          # 安全、CORS、初始化配置
│       │   ├── controller/      # MVC 的 C：REST 接口
│       │   ├── service/         # 业务逻辑
│       │   ├── repository/      # 数据访问（JPA）
│       │   ├── entity/          # JavaBean / 实体
│       │   ├── dto/             # 请求与响应对象
│       │   ├── security/        # JWT 过滤器与工具
│       │   └── common/          # 统一响应、异常、XSS 工具
│       └── resources/
│           ├── application.yml
│           └── db/             # 建表（schema.sql）与预置专业（data.sql），启动时幂等执行
├── frontend/                    # Vue 3 前端（Vite）
│   ├── package.json / vite.config.js / index.html
│   ├── .env.development / .env.production
│   └── src/
│       ├── main.js / App.vue
│       ├── api/                 # axios 封装（Ajax）
│       ├── router/ stores/      # 路由与状态
│       ├── views/               # 页面
│       └── styles/
├── deploy/                      # 部署用 Docker/Nginx 文件
│   ├── docker-compose.yml
│   ├── .env.example
│   └── nginx/hongyan-api.conf
└── README.md
```

## 本地运行

### 0. 环境要求

- JDK 25（Gradle toolchain 已锁定）
- Node.js 22 LTS（含 npm）
- 数据库无需安装：使用 SQLite 文件数据库，首次启动自动创建 `hongyan.db`

### 1. 后端

```bash
cd backend
./gradlew bootRun
```

可选环境变量（都有开发默认值，本地开箱即用）：

| 变量 | 默认值 | 说明 |
| --- | --- | --- |
| `DB_URL` | `jdbc:sqlite:hongyan.db` | SQLite 文件位置 |
| `JWT_SECRET` | 内置开发密钥 | JWT 签名密钥（生产环境必须更换为 32 位以上随机串） |
| `ADMIN_USERNAME` / `ADMIN_PASSWORD` | `admin` / `Admin@123456` | 首次启动自动创建的管理员 |
| `CORS_ORIGINS` | `http://localhost:5173` | 允许的前端来源 |

首次启动时自动建表、写入 8 个预置专业（数学与计算机学院、生命科学学院各 4 个）并创建管理员账号（脚本幂等，重启不会重复插入）。

> 后端统一挂载 `context-path: /hongyan`，本机接口完整地址形如
> `http://localhost:8080/hongyan/api/auth/login`，健康检查 `http://localhost:8080/hongyan/actuator/health`。

### 2. 前端

```bash
cd frontend
npm install
npm run dev   # http://localhost:5173
```

开发环境 `VITE_API_BASE=/api`，由 Vite 代理到 `http://localhost:8080/hongyan`（见 `vite.config.js`）。

### 3. 联调流程（验收）

1. 注册测试学号 → 直接登录应提示「账号待管理员审核」
2. 管理员登录 → 账户审核 → 通过
3. 学生重新登录 → 完善资料（专业/班级/入校年份…）→ 保存
4. 再注册并审核通过一个账号 → 互相能在「同学通讯录」中检索到
5. 管理员删除已通过账户 → 应被拒绝；禁用后该用户登录提示已禁用
6. 姓名/单位输入 `<script>alert(1)</script>` → 列表原文显示、不弹窗（XSS 防护）
7. 关键字输入 `' or 1=1 --` → 返回空结果而非全表（SQL 注入防护）

## 接口清单

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | 公开 | 学生注册（待审核） |
| POST | `/api/auth/login` | 公开 | 登录，返回 JWT |
| GET | `/api/auth/me` | 登录 | 当前用户（含最近登录时间、登录次数） |
| GET | `/api/majors` | 公开 | 启用中的专业列表 |
| GET / PUT | `/api/profile` | 登录 | 本人通讯录读 / 写 |
| GET | `/api/directory` | 登录 | 同学通讯录分页检索（专业/入校年份/关键字） |
| GET | `/api/admin/accounts` | 管理员 | 学生账户分页检索 |
| POST | `/api/admin/accounts/{id}/approve\|reject\|disable\|enable` | 管理员 | 审核/禁用/启用 |
| DELETE | `/api/admin/accounts/{id}` | 管理员 | 删除未审核账户 |
| GET / POST / PUT / DELETE | `/api/admin/majors[/{id}]` | 管理员 | 专业字典维护（停用而非物理删除） |

## 部署

见 `deploy/` 目录：

- `deploy/docker-compose.yml`：Spring Boot 容器（宿主机只开 `127.0.0.1:18080`，SQLite 文件挂 `data` 卷持久化）
- `deploy/nginx/hongyan-api.conf`：在 `api.gymesium.cn` 的 server 块 include 本片段，只接管 `/hongyan/` 路径
- 前端 `npm run build` 后部署到 Cloudflare Pages（构建目录 `dist`、根目录 `frontend`、环境变量 `VITE_API_BASE=https://api.gymesium.cn/hongyan/api`），`public/_redirects` 提供 SPA 回退

完整步骤参考《部署方法》。
