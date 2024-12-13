<template>
  <div class="app">
    <Navbar v-if="showNavbar" />
    <router-view />
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';
import { useRoute } from 'vue-router';
import Navbar from '@/components/layout/Navbar.vue';

export default defineComponent({
  components: {
    Navbar
  },
  
  setup() {
    const route = useRoute();
    
    const showNavbar = computed(() => {
      return route.name !== 'Login';
    });
    
    return {
      showNavbar
    };
  }
});
</script>

<style>
.app {
  min-height: 100vh;
}
</style> 