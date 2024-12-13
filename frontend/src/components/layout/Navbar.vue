<template>
  <el-menu mode="horizontal" :router="true">
    <el-menu-item index="/">首页</el-menu-item>
    <el-menu-item index="/cases">用例管理</el-menu-item>
    <el-menu-item index="/experiments">试验管理</el-menu-item>
    <el-menu-item index="/monitor">监控系统</el-menu-item>
    
    <div class="flex-spacer"></div>
    
    <el-dropdown v-if="isAuthenticated" @command="handleCommand">
      <span class="user-dropdown">
        {{ userInfo?.username }}
        <el-icon><arrow-down /></el-icon>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    
    <el-menu-item v-else index="/login">
      登录
    </el-menu-item>
  </el-menu>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { ArrowDown } from '@element-plus/icons-vue';

export default defineComponent({
  components: {
    ArrowDown
  },
  
  setup() {
    const router = useRouter();
    const authStore = useAuthStore();
    
    const isAuthenticated = computed(() => authStore.isAuthenticated);
    const userInfo = computed(() => authStore.userInfo);
    
    const handleCommand = (command: string) => {
      if (command === 'logout') {
        authStore.logout();
        router.push('/login');
      }
    };
    
    return {
      isAuthenticated,
      userInfo,
      handleCommand
    };
  }
});
</script>

<style scoped>
.flex-spacer {
  flex: 1;
}

.user-dropdown {
  padding: 0 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}
</style> 