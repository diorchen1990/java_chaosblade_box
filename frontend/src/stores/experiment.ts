import { defineStore } from 'pinia'
import axios from 'axios'

interface ExperimentState {
  experiments: any[]
  loading: boolean
}

export const useExperimentStore = defineStore('experiment', {
  state: (): ExperimentState => ({
    experiments: [],
    loading: false
  }),

  actions: {
    async createExperiment(data: any) {
      try {
        this.loading = true
        const response = await axios.post('/api/experiments', data)
        return response.data
      } finally {
        this.loading = false
      }
    },

    async fetchExperiments() {
      try {
        this.loading = true
        const response = await axios.get('/api/experiments')
        this.experiments = response.data
      } finally {
        this.loading = false
      }
    }
  }
}) 