<template>
  <div class="cases-container">
    <el-table :data="cases" style="width: 100%">
      <el-table-column prop="name" label="用例名称" />
      <el-table-column prop="type" label="用例类型" />
      <el-table-column prop="status" label="状态" />
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button @click="runCase(scope.row)">执行</el-button>
          <el-button @click="editCase(scope.row)">编辑</el-button>
          <el-button @click="deleteCase(scope.row)" type="danger">删除</el-button>
          <el-button @click="toggleFavorite(scope.row)">
            {{ scope.row.isFavorite ? '取消收藏' : '收藏' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'
import { useExperimentStore } from '@/stores/experiment'

interface Case {
  id: number
  name: string
  type: string
  status: string
  isFavorite: boolean
}

export default defineComponent({
  setup() {
    const experimentStore = useExperimentStore()
    const cases = ref<Case[]>([])

    const runCase = (row: Case) => {
      experimentStore.runExperiment(row.id)
    }

    const editCase = (row: Case) => {
      // 实现编辑逻辑
    }

    const deleteCase = async (row: Case) => {
      await experimentStore.deleteExperiment(row.id)
    }

    const toggleFavorite = (row: Case) => {
      experimentStore.toggleFavorite(row.id)
    }

    onMounted(async () => {
      cases.value = await experimentStore.fetchCases()
    })

    return {
      cases,
      runCase,
      editCase,
      deleteCase,
      toggleFavorite
    }
  }
})
</script> 