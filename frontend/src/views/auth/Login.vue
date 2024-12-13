<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>登录</h2>
      </template>
      
      <el-form 
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent="handleLogin"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            native-type="submit"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import type { FormInstance } from 'element-plus';

export default defineComponent({
  setup() {
    const router = useRouter();
    const authStore = useAuthStore();
    const formRef = ref<FormInstance>();
    const loading = ref(false);

    const form = reactive({
      username: '',
      password: ''
    });

    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    };

    const handleLogin = async () => {
      if (!formRef.value) return;
      
      try {
        await formRef.value.validate();
        loading.value = true;
        
        const success = await authStore.login(form);
        if (success) {
          router.push('/');
        } else {
          ElMessage.error('登录失败，请检查用户名和密码');
        }
      } catch (error) {
        console.error('Login error:', error);
      } finally {
        loading.value = false;
      }
    };

    return {
      form,
      rules,
      formRef,
      loading,
      handleLogin
    };
  }
});
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.login-card {
  width: 400px;
}
</style> 