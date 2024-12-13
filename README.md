# Chaos Platform (混沌测试平台)

基于 ChaosBlade 的混沌工程测试平台，提供一站式混沌测试解决方案。

## 功能特性

### 故障注入类型
- Java 应用故障
  - ⚡ 方法延迟注入
  - 💥 异常注入
  - 🔄 线程池故障
  - 🗑️ GC 压力模拟
  - 📈 CPU 满载模拟
  - 💾 内存溢出模拟
  
- 数据库故障
  - 🔌 连接超时
  - 🐢 慢查询模拟
  - 🌊 连接池满载
  - ❌ SQL 执行异常
  
- 分布式系统故障
  - 🕒 服务调用延迟
  - 🔒 网络分区模拟
  - 📨 消息延迟投递
  - 🚫 RPC 调用异常
  - ⏱️ Dubbo 服务超时
  - 📬 Kafka 消息延迟

### 核心功能
- 📋 用例管理
  - 用例的增删改查
  - 用例收藏与复用
  - 批量导入导出
  - 用例分类管理
  - 用例执行历史

- 📊 监控系统
  - 实时监控目标系统
  - 多维度指标采集
  - 可视化数据展示
  - 告警配置管理

- 🔌 探针管理
  - 自动安装探针
  - 探针状态监控
  - 探针生命周期管理

## 快速开始

### 环境要求
- JDK 11+
- Maven 3.6+
- Node.js 16+
- Docker & Docker Compose
- MySQL 8.0+
- Kafka 2.8+

### 本地开发
1. 克隆项目
```bash
git clone https://github.com/your-org/chaos-platform.git
cd chaos-platform
```

2. 启动后端服务
```bash
cd backend
mvn spring-boot:run
```

3. 启动前端服务
```bash
cd frontend
npm install
npm run dev
```

### Docker 部署
1. 构建镜像
```bash
docker-compose build
```

2. 启动服务
```bash
docker-compose up -d
```

3. 访问系统
- 前端: http://localhost:80
- 后端: http://localhost:8080
- 监控: http://localhost:9090

## 系统架构

### 技术栈
- 前端
  - Vue 3 + TypeScript
  - Element Plus
  - Pinia
  - Vue Router
  - ECharts

- 后端
  - Spring Boot 2.7
  - Spring Security
  - Spring Data JPA
  - ChaosBlade
  - Micrometer
  - Kafka

- 监控
  - Prometheus
  - Grafana
  - ELK Stack

### 架构图
```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Frontend  │────▶│   Backend   │────▶│  ChaosBlade │
└─────────────┘     └─────────────┘     └─────────────┘
                          │
                    ┌─────┴─────┐
                    │           │
              ┌─────▼─┐   ┌────▼────┐
              │ MySQL │   │  Kafka  │
              └───────┘   └─────────┘
```

## 使用指南

### 用例管理
1. 创建用例
   - 选择故障类型
   - 配置故障参数
   - 设置执行条件

2. 执行用例
   - 单次执行
   - 定时执行
   - 批量执行

3. 查看结果
   - 实时状态
   - 执行日志
   - 影响分析

### 监控配置
1. 指标配置
   - 系统指标
   - 业务指标
   - 自定义指标

2. 告警规则
   - 阈值告警
   - 趋势告警
   - 组合告警

## 常见问题

### 探针安装失败
- 检查目标系统权限
- 验证网络连接
- 查看安装日志
```bash
docker logs chaos-platform-backend
tail -f /var/log/chaosblade-install.log
```

### 实验执行失败
- 验证探针状态
```bash
curl http://localhost:8080/api/probes/status
```
- 检查实验参数
- 查看错误日志

### 监控数据异常
- 检查指标采集配置
```bash
curl http://localhost:8080/actuator/metrics
```
- 验证 Prometheus 连接
- 查看告警规则

## 最佳实践

### 1. 实验管理
- 从小规模开始测试
- 设置合理的故障范围
- 准备回滚方案

### 2. 监控配置
- 设置合适的采集频率
- 配置重要指标告警
- 保留足够的历史数据

### 3. 安全建议
- 使用强密码
- 限制 API 访问
- 定期更新依赖

## 贡献指南

欢迎贡献代码或提出建议！请查看我们的[贡献指南](CONTRIBUTING.md)。

## 许可证

本项目采用 [Apache 2.0 许可证](LICENSE)。

