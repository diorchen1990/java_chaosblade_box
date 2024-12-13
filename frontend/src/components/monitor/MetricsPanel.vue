<template>
  <div class="metrics-panel">
    <el-row :gutter="20">
      <!-- 实验执行统计 -->
      <el-col :span="8">
        <el-card>
          <template #header>实验执行统计</template>
          <v-chart :option="experimentChartOption" />
        </el-card>
      </el-col>
      
      <!-- 系统资源监控 -->
      <el-col :span="8">
        <el-card>
          <template #header>系统资源监控</template>
          <v-chart :option="resourceChartOption" />
        </el-card>
      </el-col>
      
      <!-- 探针状态 -->
      <el-col :span="8">
        <el-card>
          <template #header>探针状态</template>
          <el-table :data="probeStatus">
            <el-table-column prop="target" label="目标" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useMonitorStore } from '@/stores/monitor'
import type { ECOption } from '@/types/echarts'

const monitorStore = useMonitorStore()
const refreshInterval = ref<number>()

const experimentChartOption = ref<ECOption>({
  title: {
    text: '实验执行统计'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'time'
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '成功',
      type: 'line',
      data: []
    },
    {
      name: '失败',
      type: 'line',
      data: []
    }
  ]
})

// 定时刷新数据
onMounted(() => {
  fetchData()
  refreshInterval.value = window.setInterval(fetchData, 5000)
})

onUnmounted(() => {
  if (refreshInterval.value) {
    clearInterval(refreshInterval.value)
  }
})

const fetchData = async () => {
  await monitorStore.fetchMetrics()
  updateCharts()
}

const updateCharts = () => {
  // 更新图表数据
  experimentChartOption.value.series[0].data = monitorStore.experimentMetrics
    .filter(m => m.result === 'success')
    .map(m => [m.timestamp, m.count])
    
  experimentChartOption.value.series[1].data = monitorStore.experimentMetrics
    .filter(m => m.result === 'failure')
    .map(m => [m.timestamp, m.count])
}
</script> 