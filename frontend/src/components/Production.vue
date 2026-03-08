<script setup lang="ts">
import { ref } from 'vue';
import { productionAPI } from '../services/api';
import type { ProductionPlan } from '../types';

const plan = ref<ProductionPlan | null>(null);
const loading = ref(false);
const error = ref('');

async function loadOptimization() {
  loading.value = true;
  error.value = '';
  plan.value = null;

  try {
    plan.value = await productionAPI.getOptimization();
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'Failed to load production plan';
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="container">
    <div class="header">
      <h2>Production Optimization</h2>
      <button @click="loadOptimization" class="btn-primary" :disabled="loading">
        {{ loading ? 'Loading...' : 'Calculate Optimization' }}
      </button>
    </div>

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>Calculating optimal production plan...</p>
    </div>

    <div v-else-if="plan" class="results">
      <div class="summary-card">
        <h3>Total Expected Revenue</h3>
        <div class="revenue-amount">{{ plan.totalExpectedRevenue.toFixed(2) }}</div>
      </div>

      <div class="plan-items">
        <h3>Production Plan</h3>
        <div v-if="plan.items.length === 0" class="empty-state">
          No production plan available. Make sure you have products and raw materials configured.
        </div>
        <div v-else class="items-grid">
          <div v-for="(item, index) in plan.items" :key="index" class="plan-card">
            <div class="plan-header">
              <span class="product-code">{{ item.productCode }}</span>
              <span class="badge">{{ item.quantityToProduce }} units</span>
            </div>
            <h4>{{ item.productName }}</h4>
            <div class="revenue">
              <span class="label">Expected Revenue:</span>
              <span class="value">{{ item.expectedRevenue.toFixed(2) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="placeholder">
      <div class="placeholder-icon">📊</div>
      <p>Click "Calculate Optimization" to generate the production plan</p>
    </div>
  </div>
</template>

<style scoped>
.container {
  padding: 2.5rem 1.5rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2.5rem;
  gap: 1rem;
}

h2 {
  margin: 0;
  font-size: 1.875rem;
  font-weight: 700;
  letter-spacing: -0.02em;
  color: #0f172a;
}

.btn-primary {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
  font-size: 0.9rem;
  letter-spacing: 0.3px;
  white-space: nowrap;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(59, 130, 246, 0.4);
}

.btn-primary:disabled {
  background: linear-gradient(135deg, #bfdbfe 0%, #93c5fd 100%);
  cursor: not-allowed;
  transform: none;
}

.error {
  background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
  color: #7f1d1d;
  padding: 1rem 1.25rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  border: 1px solid #fecaca;
  box-shadow: 0 2px 8px rgba(220, 38, 38, 0.1);
}

.loading {
  text-align: center;
  padding: 4rem 1rem;
}

.spinner {
  width: 60px;
  height: 60px;
  border: 4px solid rgba(59, 130, 246, 0.1);
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1.5rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading p {
  color: #64748b;
  margin: 0;
  font-size: 1rem;
  font-weight: 500;
}

.placeholder {
  text-align: center;
  padding: 5rem 1rem;
  color: #64748b;
}

.placeholder-icon {
  font-size: 5rem;
  margin-bottom: 1.5rem;
  opacity: 0.8;
}

.placeholder p {
  margin: 0;
  font-size: 1.125rem;
  font-weight: 500;
}

.results {
  animation: fadeInUp 0.4s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.summary-card {
  background: linear-gradient(135deg, #3b82f6 0%, #1e40af 100%);
  background-size: 200% 200%;
  animation: gradientShift 8s ease infinite;
  color: white;
  padding: 3rem 2rem;
  border-radius: 16px;
  margin-bottom: 3rem;
  text-align: center;
  box-shadow: 0 20px 40px rgba(59, 130, 246, 0.2);
  position: relative;
  overflow: hidden;
}

.summary-card::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  pointer-events: none;
}

@keyframes gradientShift {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.summary-card h3 {
  margin: 0 0 1rem 0;
  font-size: 0.95rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1px;
  opacity: 0.85;
  position: relative;
  z-index: 1;
}

.revenue-amount {
  font-size: 3.5rem;
  font-weight: 900;
  letter-spacing: -0.02em;
  position: relative;
  z-index: 1;
}

.plan-items {
  margin-top: 2.5rem;
}

.plan-items h3 {
  margin-bottom: 1.5rem;
  font-size: 1.375rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.01em;
}

.empty-state {
  text-align: center;
  padding: 4rem 1.5rem;
  color: #64748b;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 12px;
  border: 2px dashed #cbd5e1;
  font-weight: 500;
}

.items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.plan-card {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 1.5rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.plan-card::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.1) 0%, transparent 70%);
  transition: all 0.3s ease;
}

.plan-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 40px rgba(59, 130, 246, 0.15);
  border-color: #3b82f6;
}

.plan-card:hover::before {
  top: -30%;
  right: -30%;
}

.plan-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  position: relative;
  z-index: 1;
}

.product-code {
  font-family: 'Courier New', monospace;
  font-size: 0.75rem;
  font-weight: 700;
  color: #475569;
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%);
  padding: 0.35rem 0.65rem;
  border-radius: 6px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.badge {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  font-size: 0.8rem;
  font-weight: 700;
  padding: 0.35rem 0.85rem;
  border-radius: 20px;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.plan-card h4 {
  margin: 0 0 1.25rem 0;
  font-size: 1.25rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.01em;
  position: relative;
  z-index: 1;
}

.revenue {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 1rem;
  border-top: 2px solid #e2e8f0;
  position: relative;
  z-index: 1;
}

.revenue .label {
  font-size: 0.9rem;
  color: #64748b;
  font-weight: 600;
}

.revenue .value {
  font-size: 1.375rem;
  font-weight: 800;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

@media (prefers-color-scheme: dark) {
  h2 {
    color: #f1f5f9;
  }

  .empty-state {
    background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
    border-color: #334155;
    color: #94a3b8;
  }

  .plan-card {
    background: #1e293b;
    border-color: #334155;
  }

  .plan-card:hover {
    border-color: #3b82f6;
  }

  .plan-card h4 {
    color: #f1f5f9;
  }

  .product-code {
    background: linear-gradient(135deg, #334155 0%, #475569 100%);
    color: #cbd5e1;
  }

  .revenue {
    border-top-color: #334155;
  }

  .revenue .label {
    color: #94a3b8;
  }

  .error {
    background: linear-gradient(135deg, #7f1d1d 0%, #991b1b 100%);
    color: #fecaca;
    border-color: #991b1b;
  }

  .plans-items h3 {
    color: #f1f5f9;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 1.5rem 1rem;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
  }

  h2 {
    font-size: 1.5rem;
  }

  .items-grid {
    grid-template-columns: 1fr;
  }

  .summary-card {
    padding: 2rem 1.5rem;
  }

  .revenue-amount {
    font-size: 2.5rem;
  }
}
</style>
