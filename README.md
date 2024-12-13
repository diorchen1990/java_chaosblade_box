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

## 详细部署流程

### 1. 环境准备
```bash
# 安装必要工具
安装必要工具
apt-get update && apt-get install -y \
curl \
git \
docker.io \
docker-compose \
maven \
openjdk-11-jdk
安装 Node.js
curl -sL https://deb.nodesource.com/setup_16.x | bash -
apt-get install -y nodejs

### 2. 配置环境变量
bash
创建配置文件
cp deploy/.env.example deploy/.env
编辑环境变量
vim deploy/.env
必要的环境变量
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_USER=chaos
MYSQL_PASSWORD=your_password
KAFKA_SERVERS=kafka:9092
JWT_SECRET=your_jwt_secret
### 3. 数据库初始化
bash
创建数据库和用户
mysql -u root -p
CREATE DATABASE chaos_platform;
CREATE USER 'chaos'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON chaos_platform. TO 'chaos'@'%';
FLUSH PRIVILEGES;

### 4. 构建和部署

构建和部署
bash
构建镜像
cd deploy
docker-compose build
启动服务
docker-compose up -d
查看服务状态
docker-compose ps
查看日志
docker-compose logs -f

### 5. 验证部署
bash
检查服务健康状态
curl http://localhost:8080/actuator/health
检查 Prometheus 指标
curl http://localhost:8080/actuator/prometheus

## 试验创建流程

### 1. Java 应用故障注入
1.1 方法延迟注入
curl -X POST http://localhost:8080/api/experiments \
-H "Content-Type: application/json" \
-H "Authorization: Bearer ${TOKEN}" \
-d '{
"name": "用户服务延迟测试",
"type": "JAVA_METHOD_DELAY",
"params": {
"className": "com.example.UserService",
"methodName": "getUserById",
"delay": 3000,
"probability": 100,
"duration": 300
}
}'
1.2 CPU 满载测试
curl -X POST http://localhost:8080/api/experiments \
-H "Content-Type: application/json" \
-H "Authorization: Bearer ${TOKEN}" \
-d '{
"name": "CPU压力测试",
"type": "JAVA_CPU_FULL",
"params": {
"processName": "java",
"cpuCount": 2,
"duration": 600
}
}'

### 2. 分布式故障注入
bash
2.1 网络分区测试
curl -X POST http://localhost:8080/api/experiments \
-H "Content-Type: application/json" \
-H "Authorization: Bearer ${TOKEN}" \
-d '{
"name": "网络分区测试",
"type": "DIST_NETWORK_PARTITION",
"params": {
"targetIp": "192.168.1.100",
"networkInterface": "eth0",
"probability": 50,
"duration": 300
}
}'
2.2 Kafka 消息延迟
curl -X POST http://localhost:8080/api/experiments \
-H "Content-Type: application/json" \
-H "Authorization: Bearer ${TOKEN}" \
-d '{
"name": "消息延迟测试",
"type": "DIST_KAFKA_DELAY",
"params": {
"topic": "user-events",
"delay": 5000,
"cluster": "prod-cluster",
"duration": 600
}
}'

## 自定义扩展说明

### 1. 自定义故障类型
java
// 1.1 添加故障类型枚举
public enum FaultType {
// 自定义故障类型
CUSTOM_FAULT("custom-fault", "自定义故障");
}
// 1.2 创建故障执行器
@Component
public class CustomFaultExecutor implements FaultExecutor {
@Override
public void execute(FaultParams params) {
// 实现故障注入逻辑
}
@Override
public boolean supports(FaultType type) {
return type == FaultType.CUSTOM_FAULT;
}
}

### 2. 自定义探针
java
// 2.1 创建探针配置
public class CustomProbeConfig {
private String target;
private Map<String, String> properties;
// getter/setter
}
// 2.2 实现探针安装
public class CustomProbeInstaller {
public void install(CustomProbeConfig config) {
// 实现探针安装逻辑
}
}

### 3. 自定义监控指标
ava
// 3.1 添加指标收集器
@Component
public class CustomMetricsCollector {
private final MeterRegistry registry;
public void recordCustomMetric(String name, double value) {
registry.gauge(name, value);
}
}
// 3.2 添加监控配置
@Configuration
public class CustomMetricsConfig {
@Bean
public CustomMetricsCollector customMetricsCollector(MeterRegistry registry) {
return new CustomMetricsCollector(registry);
}

### 4. 自定义告警规则
yaml
4.1 在 Prometheus 规则文件中添加
groups:
name: custom-alerts
rules:
alert: CustomMetricAlert
expr: custom_metric > threshold
for: 5m
labels:
severity: warning
annotations:
summary: "Custom metric alert"

## 常见问题排查

### 1. 探针安装失败
- 检查目标系统权限
- 验证网络连接
- 查看安装日志
docker logs chaos-platform-backend
tail -f /var/log/chaosblade-install.log

### 2. 实验执行失败
- 验证探针状态
curl http://localhost:8080/api/probes/status
- 检查实验参数
- 查看错误日志

### 3. 监控数据异常
- 检查指标采集配置
- 检查实验参数
- 查看错误日志

### 3. 监控数据异常
- 检查指标采集配置
curl http://localhost:8080/actuator/metrics
- 验证 Prometheus 连接
- 查看告警规则

## 最佳实践

1. 实验管理
- 从小规模开始测试
- 设置合理的故障范围
- 准备回滚方案

2. 监控配置
- 设置合适的采集频率
- 配置重要指标告警
- 保留足够的历史数据

3. 安全建议
- 使用强密码
- 限制 API 访问
- 定期更新依赖

