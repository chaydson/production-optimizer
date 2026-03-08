<script setup lang="ts">
import { ref } from 'vue';
import RawMaterials from './components/RawMaterials.vue';
import Products from './components/Products.vue';
import Production from './components/Production.vue';

type TabType = 'raw-materials' | 'products' | 'production';

const activeTab = ref<TabType>('production');
</script>

<template>
  <div class="app">
    <header class="header">
      <div class="header-content">
        <h1>Production Management System</h1>
        <p>Optimize your production with data-driven decisions</p>
      </div>
    </header>

    <nav class="nav">
      <button
        @click="activeTab = 'production'"
        :class="{ active: activeTab === 'production' }"
        class="nav-btn"
      >
        Production Plan
      </button>
      <button
        @click="activeTab = 'products'"
        :class="{ active: activeTab === 'products' }"
        class="nav-btn"
      >
        Products
      </button>
      <button
        @click="activeTab = 'raw-materials'"
        :class="{ active: activeTab === 'raw-materials' }"
        class="nav-btn"
      >
        Raw Materials
      </button>
    </nav>

    <main class="main">
      <Production v-if="activeTab === 'production'" />
      <Products v-else-if="activeTab === 'products'" />
      <RawMaterials v-else-if="activeTab === 'raw-materials'" />
    </main>
  </div>
</template>

<style scoped>
.app {
  min-height: 100vh;
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  position: relative;
  overflow: hidden;
}

.app::before {
  content: '';
  position: fixed;
  top: 0;
  right: 0;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.1) 0%, transparent 70%);
  pointer-events: none;
  z-index: 0;
}

.header {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #334155 100%);
  background-size: 400% 400%;
  animation: gradientShift 15s ease infinite;
  color: white;
  padding: 3rem 1.5rem 2rem;
  box-shadow: 0 20px 40px rgba(15, 23, 42, 0.15);
  position: relative;
  overflow: hidden;
}

.header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 30% 50%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  pointer-events: none;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.header h1 {
  margin: 0 0 0.75rem 0;
  font-size: 2.5rem;
  font-weight: 800;
  letter-spacing: -0.03em;
  background: linear-gradient(120deg, #ffffff 0%, #e2e8f0 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.header p {
  margin: 0;
  font-size: 1.05rem;
  opacity: 0.85;
  font-weight: 300;
  letter-spacing: 0.5px;
}

@keyframes gradientShift {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.nav {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(148, 163, 184, 0.2);
  padding: 0 1.5rem;
  display: flex;
  gap: 0.5rem;
  max-width: 1200px;
  margin: 0 auto;
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-btn {
  background: none;
  border: none;
  padding: 1.125rem 1.75rem;
  font-size: 0.9rem;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  border-bottom: 3px solid transparent;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-size: 0.8rem;
}

.nav-btn::after {
  content: '';
  position: absolute;
  bottom: -3px;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #3b82f6, #06b6d4);
  transform: scaleX(0);
  transform-origin: right;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.nav-btn:hover {
  color: #1e293b;
}

.nav-btn:hover::after {
  transform: scaleX(1);
  transform-origin: left;
}

.nav-btn.active {
  color: #0f172a;
}

.nav-btn.active::after {
  transform: scaleX(1);
}

.main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
  position: relative;
  z-index: 1;
}

@media (max-width: 768px) {
  .header h1 {
    font-size: 1.75rem;
  }

  .header p {
    font-size: 0.95rem;
  }

  .header {
    padding: 2rem 1.5rem 1.5rem;
  }

  .nav {
    padding: 0 0.75rem;
    overflow-x: auto;
  }

  .nav-btn {
    padding: 1rem 1.25rem;
    font-size: 0.75rem;
    white-space: nowrap;
  }
}

@media (prefers-color-scheme: dark) {
  .app {
    background: linear-gradient(135deg, #0f172a 0%, #1a1f35 100%);
  }

  .nav {
    background: rgba(15, 23, 42, 0.8);
    border-bottom-color: rgba(71, 85, 105, 0.2);
  }

  .nav-btn {
    color: #94a3b8;
  }

  .nav-btn:hover {
    color: #e2e8f0;
  }

  .header::before {
    background: radial-gradient(circle at 30% 50%, rgba(255, 255, 255, 0.05) 0%, transparent 50%);
  }
}
</style>
