#!/bin/bash

# 清理未使用的构建缓存
docker builder prune -f

# 清理未使用的镜像
docker image prune -f

# 清理未使用的数据卷
docker volume prune -f 