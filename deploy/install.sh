#!/bin/bash

# 安装 ChaosBlade
curl -sS https://raw.githubusercontent.com/chaosblade-io/chaosblade/master/install.sh | sh

# 启动服务
docker-compose up -d 