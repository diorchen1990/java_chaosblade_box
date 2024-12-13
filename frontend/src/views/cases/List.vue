<template>
  <div class="case-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用例列表</span>
          <el-button type="primary" @click="createCase">新建用例</el-button>
        </div>
      </template>

      <el-table :data="cases" style="width: 100%">
        <el-table-column prop="name" label="用例名称" />
        <el-table-column prop="owner.username" label="创建者" />
        <el-table-column prop="createdAt" label="创建时间">
          <template #default="scope">
            {{ formatDateTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <!-- 所有者可以编辑和删除 -->
            <template v-if="isOwner(scope.row)">
              <el-button size="small" @click="editCase(scope.row)">编辑</el-button>
              <el-button 
                size="small" 
                type="danger" 
                @click="deleteCase(scope.row)"
              >删除</el-button>
            </template>
            <!-- 非所有者只能复制 -->
            <template v-else>
              <el-button size="small" @click="copyCase(scope.row)">复制</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCaseStore } from '@/stores/case'
import { useUserStore } from '@/stores/user'
import { formatDateTime } from '@/utils/date'

const router = useRouter()
const caseStore = useCaseStore()
const userStore = useUserStore()
const cases = ref([])

onMounted(async () => {
  await loadCases()
})

const loadCases = async () => {
  cases.value = await caseStore.getCases()
}

const isOwner = (testCase) => {
  return testCase.owner.id === userStore.currentUser?.id
}

const createCase = () => {
  router.push('/cases/create')
}

const editCase = (testCase) => {
  router.push(`/cases/${testCase.id}/edit`)
}

const copyCase = async (testCase) => {
  try {
    await caseStore.copyCase(testCase.id)
    ElMessage.success('用例复制成功')
    await loadCases()
  } catch (error) {
    ElMessage.error('用例复制失败')
  }
}

const deleteCase = async (testCase) => {
  try {
    await ElMessageBox.confirm('确定要删除该用例吗？')
    await caseStore.deleteCase(testCase.id)
    ElMessage.success('用例删除成功')
    await loadCases()
  } catch {
    // 取消删除
  }
}
</script> 