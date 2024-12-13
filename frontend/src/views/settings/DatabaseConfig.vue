<template>
  <div class="database-config">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据库配置</span>
          <el-button type="primary" @click="testConnection">测试连接</el-button>
        </div>
      </template>

      <el-form 
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="config-form"
      >
        <el-form-item label="数据库地址" prop="host">
          <el-input v-model="form.host" placeholder="localhost">
            <template #append>
              <el-input v-model="form.port" style="width: 80px" placeholder="3306" />
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="数据库名" prop="database">
          <el-input v-model="form.database" placeholder="chaos_platform" />
        </el-form-item>

        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="root" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="form.password" 
            type="password"
            placeholder="请输入数据库密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="连接参数">
          <el-input 
            v-model="form.params"
            type="textarea"
            :rows="3"
            placeholder="useSSL=false&characterEncoding=utf8"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="saveConfig">保存配置</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { FormInstance } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useConfigStore } from '@/stores/config'

interface FormState {
  host: string
  port: string
  database: string
  username: string
  password: string
  params: string
}

const configStore = useConfigStore()
const formRef = ref<FormInstance>()
const form = ref<FormState>({
  host: 'localhost',
  port: '3306',
  database: 'chaos_platform',
  username: 'root',
  password: '',
  params: 'useSSL=false'
})

const rules = {
  host: [{ required: true, message: '请输入数据库地址', trigger: 'blur' }],
  database: [{ required: true, message: '请输入数据库名', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

onMounted(async () => {
  await loadConfig()
})

const loadConfig = async () => {
  const configs = await configStore.fetchConfigsByCategory('DATABASE')
  form.value = {
    host: configs.find(c => c.key === 'spring.datasource.host')?.value || 'localhost',
    port: configs.find(c => c.key === 'spring.datasource.port')?.value || '3306',
    database: configs.find(c => c.key === 'spring.datasource.database')?.value || 'chaos_platform',
    username: configs.find(c => c.key === 'spring.datasource.username')?.value || 'root',
    password: '',
    params: configs.find(c => c.key === 'spring.datasource.params')?.value || 'useSSL=false'
  }
}

const saveConfig = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        await configStore.updateConfigs({
          'spring.datasource.host': form.value.host,
          'spring.datasource.port': form.value.port,
          'spring.datasource.database': form.value.database,
          'spring.datasource.username': form.value.username,
          'spring.datasource.password': form.value.password,
          'spring.datasource.params': form.value.params
        })
        ElMessage.success('配置保存成功')
      } catch (error) {
        ElMessage.error('配置保存失败')
      }
    }
  })
}

const testConnection = async () => {
  try {
    await configStore.testDatabaseConnection(form.value)
    ElMessage.success('数据库连接测试成功')
  } catch (error) {
    ElMessage.error('数据库连接测试失败')
  }
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}
</script>

<style scoped>
.database-config {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.config-form {
  max-width: 600px;
  margin: 0 auto;
}
</style> 