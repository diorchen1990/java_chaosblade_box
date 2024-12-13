export interface ExperimentMetric {
  timestamp: number
  type: string
  result: 'success' | 'failure'
  count: number
}

export interface SystemMetric {
  timestamp: number
  target: string
  cpuUsage: number
  memoryUsage: number
  diskUsage: number
}

export interface ProbeStatus {
  target: string
  status: 'active' | 'inactive'
  lastUpdate: number
} 