<template>
  <div class="experiment-create">
    <el-form :model="form" label-width="120px" class="experiment-form">
      <!-- 基本信息 -->
      <el-form-item label="实验名称" required>
        <el-input v-model="form.name" placeholder="请输入实验名称" />
      </el-form-item>

      <!-- 故障类型选择 -->
      <el-form-item label="故障类型" required>
        <el-select v-model="form.faultType" placeholder="请选择故障类型">
          <el-option-group label="Java应用故障">
            <el-option label="方法延迟" value="JAVA_METHOD_DELAY" />
            <el-option label="方法异常" value="JAVA_METHOD_EXCEPTION" />
            <el-option label="CPU满载" value="JAVA_CPU_FULL" />
            <el-option label="内存溢出" value="JAVA_OOM" />
          </el-option-group>
          
          <el-option-group label="数据库故障">
            <el-option label="SQL超时" value="DB_TIMEOUT" />
            <el-option label="连接异常" value="DB_CONNECTION_FAIL" />
          </el-option-group>
        </el-select>
      </el-form-item>

      <!-- 动态参数配置 -->
      <template v-if="form.faultType">
        <!-- Java方法故障参数 -->
        <template v-if="isJavaMethodFault">
          <el-form-item label="目标类" required>
            <el-input v-model="form.params.className" placeholder="com.example.Service" />
          </el-form-item>
          
          <el-form-item label="目标方法" required>
            <el-input v-model="form.params.methodName" placeholder="methodName" />
          </el-form-item>
          
          <el-form-item label="延迟时间(ms)" v-if="form.faultType === 'JAVA_METHOD_DELAY'">
            <el-input-number v-model="form.params.delay" :min="0" />
          </el-form-item>
          
          <el-form-item label="异常类型" v-if="form.faultType === 'JAVA_METHOD_EXCEPTION'">
            <el-input v-model="form.params.exception" placeholder="java.lang.RuntimeException" />
          </el-form-item>
        </template>

        <!-- 数据库故障参数 -->
        <template v-if="isDatabaseFault">
          <el-form-item label="数据源" required>
            <el-input v-model="form.params.datasource" placeholder="数据源名称" />
          </el-form-item>
          
          <el-form-item label="超时时间(ms)" v-if="form.faultType === 'DB_TIMEOUT'">
            <el-input-number v-model="form.params.timeout" :min="0" />
          </el-form-item>
        </template>

        <!-- 通用参数 -->
        <el-form-item label="故障触发概率">
          <el-slider v-model="form.params.probability" :min="0" :max="100" />
        </el-form-item>
        
        <el-form-item label="执行时长(秒)">
          <el-input-number v-model="form.params.duration" :min="1" />
        </el-form-item>
      </template>

      <el-form-item>
        <el-button type="primary" @click="submitForm">开始实验</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive, computed } from 'vue'
import { useExperimentStore } from '@/stores/experiment'
import { ElMessage } from 'element-plus'

const experimentStore = useExperimentStore()

const form = reactive({
  name: '',
  faultType: '',
  params: {
    className: '',
    methodName: '',
    delay: 1000,
    exception: '',
    datasource: '',
    timeout: 3000,
    probability: 100,
    duration: 60
  }
})

const isJavaMethodFault = computed(() => {
  return form.faultType.startsWith('JAVA_METHOD')
})

const isDatabaseFault = computed(() => {
  return form.faultType.startsWith('DB_')
})

const submitForm = async () => {
  try {
    await experimentStore.createExperiment({
      name: form.name,
      type: form.faultType,
      params: form.params
    })
    ElMessage.success('实验创建成功')
  } catch (error) {
    ElMessage.error('实验创建失败')
  }
}

const resetForm = () => {
  form.name = ''
  form.faultType = ''
  form.params = {
    className: '',
    methodName: '',
    delay: 1000,
    exception: '',
    datasource: '',
    timeout: 3000,
    probability: 100,
    duration: 60
  }
}
</script>

<style scoped>
.experiment-form {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
</style> 