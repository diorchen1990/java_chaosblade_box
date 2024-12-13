<template>
  <div class="service-fault">
    <el-form :model="form" label-width="120px">
      <!-- 基础配置 -->
      <el-form-item label="故障类型" required>
        <el-select v-model="form.faultType">
          <el-option label="服务延迟" value="DIST_SERVICE_DELAY" />
          <el-option label="网络分区" value="DIST_NETWORK_PARTITION" />
          <el-option label="消息延迟" value="DIST_MESSAGE_DELAY" />
          <el-option label="RPC异常" value="DIST_RPC_EXCEPTION" />
        </el-select>
      </el-form-item>

      <!-- 服务故障配置 -->
      <template v-if="form.faultType === 'DIST_SERVICE_DELAY'">
        <el-form-item label="目标服务" required>
          <el-input v-model="form.params.service" />
        </el-form-item>
        <el-form-item label="服务版本">
          <el-input v-model="form.params.version" />
        </el-form-item>
        <el-form-item label="延迟时间(ms)">
          <el-input-number v-model="form.params.delay" :min="0" />
        </el-form-item>
      </template>

      <!-- 网络故障配置 -->
      <template v-if="form.faultType === 'DIST_NETWORK_PARTITION'">
        <el-form-item label="目标IP" required>
          <el-input v-model="form.params.targetIp" />
        </el-form-item>
        <el-form-item label="网络接口" required>
          <el-input v-model="form.params.networkInterface" />
        </el-form-item>
        <el-form-item label="丢包率(%)">
          <el-slider v-model="form.params.probability" :min="0" :max="100" />
        </el-form-item>
      </template>

      <!-- 消息队列故障配置 -->
      <template v-if="form.faultType === 'DIST_MESSAGE_DELAY'">
        <el-form-item label="消息主题" required>
          <el-input v-model="form.params.topic" />
        </el-form-item>
        <el-form-item label="延迟时间(ms)">
          <el-input-number v-model="form.params.delay" :min="0" />
        </el-form-item>
      </template>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from 'vue'

const form = reactive({
  faultType: '',
  params: {
    service: '',
    version: '',
    delay: 1000,
    targetIp: '',
    networkInterface: '',
    probability: 100,
    topic: ''
  }
})
</script> 