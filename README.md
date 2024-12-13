# Chaos Platform (混沌测试平台)

基于 ChaosBlade 的混沌工程测试平台，提供一站式混沌测试解决方案。

## 功能特性

### 故障注入类型
- [x] Java 应用故障
  - 方法延迟注入
  - 异常注入
  - CPU 满载模拟
  - 内存溢出模拟
  - 线程池故障
  - GC 压力模拟
- [x] 数据库故障
  - 连接超时
  - 慢查询模拟
  - 连接池满载
  - SQL 执行异常
- [x] 分布式系统故障
  - 服务调用延迟
  - 网络分区模拟
  - 消息延迟投递
  - RPC 调用异常
  - Dubbo 服务超时
  - Kafka 消息延迟

### 核心功能
- [x] 用例管理
  - 用例的增删改查
  - 用例收藏与复用
  - 批量导入导出
  - 用例分类管理
  - 用例执行历史
- [x] 监控系统
  - 实时监控目标系统
  - 多维度指标采集
  - 可视化数据展示
  - 告警配置管理
- [x] 探针管理
  - 自动安装探针
  - 探针状态监控
  - 探针生命周期管理

## 技术栈

### 前端
- Vue 3 + TypeScript
- Element Plus UI
- ECharts 图表
- Pinia 状态管理
- Vite 构建工具

### 后端
- Spring Boot 2.7
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.0
- Kafka 消息队列
- ChaosBlade 故障注入

## 快速开始

### 环境要求
- JDK 11+
- Node.js 16+
- Docker & Docker Compose
- MySQL 8.0+
- Maven 3.6+

### 本地开发

1. 克隆项目
bash
git clone https://github.com/your-repo/chaos-platform.git
cd chaos-platform
2. 启动后端
bash
cd backend
mvn spring-boot:run -Dspring.profiles.active=dev
3. 启动前端
bash
cd frontend
npm install
npm run dev
4. 访问系统
前端: http://localhost:3000
后端: http://localhost:8080

### Docker 部署

1. 配置环境变量
bash
cp deploy/.env.example deploy/.env

2. 构建并启动服务

构建并启动服务
bash
cd deploy
docker-compose up -d
## 项目结构
chaos-platform/
├── frontend/ # 前端项目
│ ├── src/
│ │ ├── components/ # 公共组件
│ │ ├── views/ # 页面组件
│ │ ├── stores/ # 状态管理
│ │ └── types/ # TypeScript 类型定义
│ └── ...
├── backend/ # 后端项目
│ ├── src/
│ │ └── main/
│ │ ├── java/
│ │ │ └── com/chaos/platform/
│ │ │ ├── controller/ # 控制器
│ │ │ ├── service/ # 业务逻辑
│ │ │ ├── model/ # 数据模型
│ │ │ └── config/ # 配置类
│ │ └── resources/
│ │ └── application.yml # 配置文件
│ └── ...
└── deploy/ # 部署相关配置
├── docker-compose.yml
├── .env
└── Dockerfile

## API 文档

API 文档使用 Swagger 生成，访问地址：
http://localhost:8080/swagger-ui.html

## 开发指南

### 添加新的故障类型

1. 在 `FaultType` 枚举中添加新类型
2. 创建对应的 `FaultExecutor` 实现类
3. 在前端添加对应的配置界面
4. 添加相应的测试用例

### 自定义监控指标

1. 在 `MetricsCollector` 中添加新的指标收集方法
2. 在监控页面添加对应的展示组件
3. 配置告警规则（可选）

## 测试

### 单元测试
bash
后端测试
cd backend
mvn test
前端测试
cd frontend
npm run test

### 集成测试
bash
cd backend
mvn verify -P integration-test

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交改动 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 项目维护者: Your Name
- Email: your.email@example.com
- 项目主页: https://github.com/your-repo/chaos-platform

## 致谢

- [ChaosBlade](https://github.com/chaosblade-io/chaosblade)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org)