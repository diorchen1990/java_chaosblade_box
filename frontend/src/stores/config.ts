import { defineStore } from 'pinia'
import axios from 'axios'

interface Config {
  key: string
  value: string
  description: string
  category: string
}

interface DatabaseConfig {
  host: string
  port: string
  database: string
  username: string
  password: string
  params: string
}

interface ConfigState {
  configs: Config[]
  loading: boolean
}

export const useConfigStore = defineStore('config', {
  state: (): ConfigState => ({
    configs: [],
    loading: false
  }),

  actions: {
    async fetchConfigsByCategory(category: string): Promise<Config[]> {
      try {
        this.loading = true
        const response = await axios.get(`/api/configs?category=${category}`)
        return response.data
      } finally {
        this.loading = false
      }
    },

    async updateConfigs(configs: Record<string, string>): Promise<void> {
      const updates = Object.entries(configs).map(([key, value]) => 
        axios.put(`/api/configs/${key}`, { value })
      )
      await Promise.all(updates)
    },

    async testDatabaseConnection(config: DatabaseConfig): Promise<void> {
      await axios.post('/api/configs/database/test', config)
    }
  }
}) 