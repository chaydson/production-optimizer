<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { rawMaterialsAPI } from '../services/api';
import type { RawMaterialRequest, RawMaterialResponse } from '../types';

const rawMaterials = ref<RawMaterialResponse[]>([]);
const loading = ref(false);
const error = ref('');
const showForm = ref(false);

const form = ref<RawMaterialRequest>({
  code: '',
  name: '',
  stockQuantity: 0,
});

async function loadRawMaterials() {
  loading.value = true;
  error.value = '';
  try {
    rawMaterials.value = await rawMaterialsAPI.listAll();
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'Failed to load raw materials';
  } finally {
    loading.value = false;
  }
}

async function createRawMaterial() {
  if (!form.value.code || !form.value.name || form.value.stockQuantity < 0) {
    error.value = 'Please fill all fields correctly';
    return;
  }

  loading.value = true;
  error.value = '';
  try {
    await rawMaterialsAPI.create(form.value);
    form.value = { code: '', name: '', stockQuantity: 0 };
    showForm.value = false;
    await loadRawMaterials();
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'Failed to create raw material';
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  loadRawMaterials();
});
</script>

<template>
  <div class="container">
    <div class="header">
      <h2>Raw Materials</h2>
      <button @click="showForm = !showForm" class="btn-primary">
        {{ showForm ? 'Cancel' : 'Add New' }}
      </button>
    </div>

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="showForm" class="form-card">
      <h3>New Raw Material</h3>
      <form @submit.prevent="createRawMaterial">
        <div class="form-group">
          <label>Code</label>
          <input v-model="form.code" type="text" required />
        </div>
        <div class="form-group">
          <label>Name</label>
          <input v-model="form.name" type="text" required />
        </div>
        <div class="form-group">
          <label>Stock Quantity</label>
          <input v-model.number="form.stockQuantity" type="number" min="0" step="0.01" required />
        </div>
        <button type="submit" class="btn-primary" :disabled="loading">
          {{ loading ? 'Creating...' : 'Create' }}
        </button>
      </form>
    </div>

    <div v-if="loading && !showForm" class="loading">Loading...</div>

    <div v-else class="table-container">
      <table>
        <thead>
          <tr>
            <th>Code</th>
            <th>Name</th>
            <th>Stock Quantity</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in rawMaterials" :key="item.id">
            <td>{{ item.code }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.stockQuantity.toFixed(2) }}</td>
          </tr>
          <tr v-if="rawMaterials.length === 0">
            <td colspan="3" class="empty">No raw materials found</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.container {
  padding: 1.5rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

h2 {
  margin: 0;
  font-size: 1.75rem;
  font-weight: 600;
}

.btn-primary {
  background-color: #3b82f6;
  color: white;
  border: none;
  padding: 0.625rem 1.25rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}

.btn-primary:hover {
  background-color: #2563eb;
}

.btn-primary:disabled {
  background-color: #93c5fd;
  cursor: not-allowed;
}

.error {
  background-color: #fef2f2;
  color: #dc2626;
  padding: 0.75rem;
  border-radius: 6px;
  margin-bottom: 1rem;
  border: 1px solid #fecaca;
}

.form-card {
  background-color: #f9fafb;
  padding: 1.5rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  border: 1px solid #e5e7eb;
}

.form-card h3 {
  margin-top: 0;
  margin-bottom: 1rem;
  font-size: 1.25rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.375rem;
  font-weight: 500;
  font-size: 0.875rem;
}

.form-group input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 0.875rem;
  box-sizing: border-box;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #6b7280;
}

.table-container {
  overflow-x: auto;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background-color: #f9fafb;
}

th {
  padding: 0.75rem 1rem;
  text-align: left;
  font-weight: 600;
  font-size: 0.875rem;
  color: #374151;
  border-bottom: 1px solid #e5e7eb;
}

td {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #e5e7eb;
  font-size: 0.875rem;
}

tbody tr:last-child td {
  border-bottom: none;
}

tbody tr:hover {
  background-color: #f9fafb;
}

.empty {
  text-align: center;
  color: #6b7280;
  font-style: italic;
}

@media (prefers-color-scheme: dark) {
  .form-card {
    background-color: #1f2937;
    border-color: #374151;
  }

  .form-group input {
    background-color: #111827;
    border-color: #374151;
    color: #f9fafb;
  }

  .table-container {
    border-color: #374151;
  }

  thead {
    background-color: #1f2937;
  }

  th {
    color: #e5e7eb;
    border-bottom-color: #374151;
  }

  td {
    border-bottom-color: #374151;
  }

  tbody tr:hover {
    background-color: #1f2937;
  }

  .error {
    background-color: #7f1d1d;
    color: #fca5a5;
    border-color: #991b1b;
  }
}
</style>
