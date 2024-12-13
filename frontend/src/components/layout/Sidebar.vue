<template>
  <el-menu
    :default-active="activeMenu"
    class="sidebar-menu"
    :collapse="isCollapse"
  >
    <el-menu-item-group title="系统管理">
      <el-menu-item index="/settings/database">
        <el-icon><DataBase /></el-icon>
        <span>数据库配置</span>
      </el-menu-item>
    </el-menu-item-group>
  </el-menu>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { DataBase } from '@element-plus/icons-vue'

const route = useRoute()
const isCollapse = ref(false)
const activeMenu = computed(() => route.path)
</script> 