<template>
  <div class="monitor-container">
    <div class="metrics-panel">
      <el-row :gutter="20">
        <el-col :span="6" v-for="metric in metrics" :key="metric.id">
          <el-card>
            <template #header>
              <div class="metric-header">
                {{ metric.name }}
              </div>
            </template>
            <div class="metric-value">
              {{ metric.value }}
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <div class="charts-panel">
      <v-chart :option="chartOption" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'
import { use } from 'echarts/core'
import VChart from 'vue-echarts'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent } from 'echarts/components'

use([CanvasRenderer, LineChart, GridComponent, TooltipComponent])

interface Metric {
  id: number
  name: string
  value: string | number
}

export default defineComponent({
  components: {
    VChart
  },
  setup() {
    const metrics = ref<Metric[]>([])
    const chartOption = ref({
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        type: 'line'
      }]
    })

    onMounted(async () => {
      // 从后端获取监控数据
    })

    return {
      metrics,
      chartOption
    }
  }
})
</script> 