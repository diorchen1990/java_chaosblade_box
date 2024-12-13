<template>
  <div class="case-list page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="left">
            <h2 class="title">用例列表</h2>
            <el-input
              v-model="searchQuery"
              placeholder="搜索用例"
              prefix-icon="Search"
              clearable
              class="search-input"
            />
          </div>
          <div class="right">
            <el-button type="primary" @click="createCase">
              <el-icon><Plus /></el-icon>新建用例
            </el-button>
          </div>
        </div>
      </template>

      <el-table 
        :data="filteredCases"
        v-loading="loading"
        row-key="id"
        class="case-table"
      >
        <el-table-column prop="name" label="用例名称">
          <template #default="{ row }">
            <div class="case-name">
              <el-icon><Document /></el-icon>
              <span>{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="owner.username" label="创建者">
          <template #default="{ row }">
            <el-tag size="small">{{ row.owner.username }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            <div class="time-info">
              <el-icon><Timer /></el-icon>
              <span>{{ formatDateTime(row.createdAt) }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <template v-if="isOwner(row)">
                <el-button 
                  link 
                  type="primary" 
                  @click="editCase(row)"
                >
                  <el-icon><Edit /></el-icon>编辑
                </el-button>
                <el-button 
                  link 
                  type="danger" 
                  @click="deleteCase(row)"
                >
                  <el-icon><Delete /></el-icon>删除
                </el-button>
              </template>
              <template v-else>
                <el-button 
                  link 
                  type="info" 
                  @click="copyCase(row)"
                >
                  <el-icon><CopyDocument /></el-icon>复制
                </el-button>
              </template>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="totalCount"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Document, Edit, Delete, CopyDocument, 
  Plus, Search, Timer 
} from '@element-plus/icons-vue'
import { useCaseStore } from '@/stores/case'
import { useUserStore } from '@/stores/user'
import { formatDateTime } from '@/utils/date'

const router = useRouter()
const caseStore = useCaseStore()
const userStore = useUserStore()
const cases = ref([])

const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)

const filteredCases = computed(() => {
  return cases.value.filter(item => 
    item.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

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

const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadCases()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadCases()
}
</script>

<style lang="scss" scoped>
.case-list {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .left {
      display: flex;
      align-items: center;
      gap: 20px;
      
      .title {
        margin: 0;
        font-size: 20px;
        font-weight: 600;
      }
      
      .search-input {
        width: 300px;
      }
    }
  }
  
  .case-table {
    margin: 20px 0;
    
    .case-name {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .el-icon {
        color: var(--primary-color);
      }
    }
    
    .time-info {
      display: flex;
      align-items: center;
      gap: 6px;
      color: var(--info-color);
      
      .el-icon {
        font-size: 14px;
      }
    }
    
    .action-buttons {
      display: flex;
      gap: 12px;
      
      .el-button {
        padding: 4px 8px;
        
        .el-icon {
          margin-right: 4px;
        }
      }
    }
  }
  
  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
}
</style> 