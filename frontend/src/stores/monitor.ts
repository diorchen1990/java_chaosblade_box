import { defineStore } from 'pinia'
import axios from 'axios'

interface MetricsState {
  experimentMetrics: any[]
  systemMetrics: any[]
  probeStatus: any[]
  loading: boolean
}

export const useMonitorStore = defineStore('monitor', {
  state: (): MetricsState => ({
    experimentMetrics: [],
    systemMetrics: [],
    probeStatus: [],
    loading: false
  }),

  actions: {
    async fetchMetrics() {
      try {
        this.loading = true
        const [experiments, system, probes] = await Promise.all([
          axios.get('/api/metrics/experiments'),
          axios.get('/api/metrics/system'),
          axios.get('/api/metrics/probes')
        ])
        this.experimentMetrics = experiments.data
        this.systemMetrics = system.data
        this.probeStatus = probes.data
      } finally {
        this.loading = false
      }
    }
  }
}) 