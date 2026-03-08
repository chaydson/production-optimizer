<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { productsAPI, rawMaterialsAPI } from '../services/api';
import type { ProductRequest, ProductResponse, RawMaterialResponse, CompositionRequest } from '../types';

const products = ref<ProductResponse[]>([]);
const rawMaterials = ref<RawMaterialResponse[]>([]);
const loading = ref(false);
const error = ref('');
const showForm = ref(false);

const form = ref<ProductRequest>({
  code: '',
  name: '',
  price: 0,
  compositions: [],
});

const newComposition = ref<CompositionRequest>({
  rawMaterialId: '',
  quantity: 0,
});

async function loadProducts() {
  loading.value = true;
  error.value = '';
  try {
    products.value = await productsAPI.listAll();
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'Failed to load products';
  } finally {
    loading.value = false;
  }
}

async function loadRawMaterials() {
  try {
    rawMaterials.value = await rawMaterialsAPI.listAll();
  } catch (e) {
    console.error('Failed to load raw materials', e);
  }
}

function addComposition() {
  if (!newComposition.value.rawMaterialId || newComposition.value.quantity <= 0) {
    return;
  }
  form.value.compositions.push({ ...newComposition.value });
  newComposition.value = { rawMaterialId: '', quantity: 0 };
}

function removeComposition(index: number) {
  form.value.compositions.splice(index, 1);
}

function getRawMaterialName(id: string): string {
  const rm = rawMaterials.value.find(r => r.id === id);
  return rm ? `${rm.code} - ${rm.name}` : id;
}

async function createProduct() {
  if (!form.value.code || !form.value.name || form.value.price <= 0) {
    error.value = 'Please fill all fields correctly';
    return;
  }

  loading.value = true;
  error.value = '';
  try {
    await productsAPI.create(form.value);
    form.value = { code: '', name: '', price: 0, compositions: [] };
    showForm.value = false;
    await loadProducts();
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'Failed to create product';
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  loadProducts();
  loadRawMaterials();
});
</script>

<template>
  <div class="container">
    <div class="header">
      <h2>Products</h2>
      <button @click="showForm = !showForm" class="btn-primary">
        {{ showForm ? 'Cancel' : 'Add New' }}
      </button>
    </div>

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="showForm" class="form-card">
      <h3>New Product</h3>
      <form @submit.prevent="createProduct">
        <div class="form-group">
          <label>Code</label>
          <input v-model="form.code" type="text" required />
        </div>
        <div class="form-group">
          <label>Name</label>
          <input v-model="form.name" type="text" required />
        </div>
        <div class="form-group">
          <label>Price</label>
          <input v-model.number="form.price" type="number" min="0" step="0.01" required />
        </div>

        <div class="compositions-section">
          <h4>Compositions</h4>
          <div class="composition-form">
            <select v-model="newComposition.rawMaterialId">
              <option value="">Select raw material</option>
              <option v-for="rm in rawMaterials" :key="rm.id" :value="rm.id">
                {{ rm.code }} - {{ rm.name }}
              </option>
            </select>
            <input
              v-model.number="newComposition.quantity"
              type="number"
              min="0"
              step="0.01"
              placeholder="Quantity"
            />
            <button type="button" @click="addComposition" class="btn-secondary">Add</button>
          </div>

          <ul v-if="form.compositions.length > 0" class="composition-list">
            <li v-for="(comp, index) in form.compositions" :key="index">
              <span>{{ getRawMaterialName(comp.rawMaterialId) }} - Qty: {{ comp.quantity }}</span>
              <button type="button" @click="removeComposition(index)" class="btn-remove">Remove</button>
            </li>
          </ul>
          <p v-else class="empty-text">No compositions added</p>
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
            <th>Price</th>
            <th>Compositions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <td>{{ product.code }}</td>
            <td>{{ product.name }}</td>
            <td>{{ product.price.toFixed(2) }}</td>
            <td>
              <ul class="inline-list">
                <li v-for="(comp, idx) in product.compositions" :key="idx">
                  {{ getRawMaterialName(comp.rawMaterialId) }} ({{ comp.quantity }})
                </li>
              </ul>
            </td>
          </tr>
          <tr v-if="products.length === 0">
            <td colspan="4" class="empty">No products found</td>
          </tr>
        </tbody>
      </table>
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

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(59, 130, 246, 0.4);
}

.btn-primary:disabled {
  background: linear-gradient(135deg, #bfdbfe 0%, #93c5fd 100%);
  cursor: not-allowed;
  transform: none;
}

.btn-secondary {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
  font-size: 0.85rem;
}

.btn-secondary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}

.btn-remove {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
  border: none;
  padding: 0.3rem 0.75rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.75rem;
  font-weight: 600;
  transition: all 0.2s;
}

.btn-remove:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
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

.form-card {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  padding: 2rem;
  border-radius: 12px;
  margin-bottom: 2rem;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 20px rgba(59, 130, 246, 0.08);
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-card h3 {
  margin-top: 0;
  margin-bottom: 1.5rem;
  font-size: 1.375rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.01em;
}

.form-group {
  margin-bottom: 1.25rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  font-size: 0.9rem;
  color: #334155;
  letter-spacing: 0.2px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 0.9rem;
  box-sizing: border-box;
  background: white;
  color: #0f172a;
  transition: all 0.2s;
  font-family: inherit;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.compositions-section {
  margin: 2rem 0;
  padding: 1.5rem;
  background: white;
  border-radius: 10px;
  border: 1px solid #e2e8f0;
}

.compositions-section h4 {
  margin-top: 0;
  margin-bottom: 1rem;
  font-size: 1.05rem;
  font-weight: 700;
  color: #0f172a;
}

.composition-form {
  display: grid;
  grid-template-columns: 2fr 1fr auto;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.composition-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.composition-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 8px;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
  border: 1px solid #e2e8f0;
  transition: all 0.2s;
}

.composition-list li:hover {
  border-color: #cbd5e1;
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
}

.empty-text {
  color: #64748b;
  font-size: 0.875rem;
  font-style: italic;
  margin: 0;
}

.loading {
  text-align: center;
  padding: 3rem 1.5rem;
  color: #64748b;
  font-weight: 500;
}

.table-container {
  overflow-x: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

th {
  padding: 1rem 1.25rem;
  text-align: left;
  font-weight: 700;
  font-size: 0.875rem;
  color: #334155;
  border-bottom: 2px solid #e2e8f0;
  text-transform: uppercase;
  letter-spacing: 0.4px;
}

td {
  padding: 1rem 1.25rem;
  border-bottom: 1px solid #e2e8f0;
  font-size: 0.875rem;
  color: #334155;
}

tbody tr {
  transition: all 0.2s;
}

tbody tr:last-child td {
  border-bottom: none;
}

tbody tr:hover {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

.empty {
  text-align: center;
  color: #64748b;
  font-style: italic;
  padding: 2rem;
}

.inline-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.inline-list li {
  display: inline;
  margin-right: 0.75rem;
  padding: 0.25rem 0.5rem;
  background: rgba(59, 130, 246, 0.1);
  border-radius: 4px;
  font-size: 0.8rem;
  color: #2563eb;
  font-weight: 600;
}

@media (prefers-color-scheme: dark) {
  h2 {
    color: #f1f5f9;
  }

  .form-card {
    background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
    border-color: #334155;
  }

  .form-card h3 {
    color: #f1f5f9;
  }

  .form-group label {
    color: #cbd5e1;
  }

  .form-group input,
  .form-group select {
    background-color: #0f172a;
    border-color: #334155;
    color: #f1f5f9;
  }

  .form-group input:focus,
  .form-group select:focus {
    border-color: #3b82f6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
  }

  .compositions-section {
    background-color: #0f172a;
    border-color: #334155;
  }

  .compositions-section h4 {
    color: #f1f5f9;
  }

  .composition-list li {
    background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
    border-color: #334155;
  }

  .composition-list li:hover {
    border-color: #475569;
    background: linear-gradient(135deg, #0f172a 0%, #1a1f35 100%);
  }

  .table-container {
    border-color: #334155;
  }

  thead {
    background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
  }

  th {
    color: #cbd5e1;
    border-bottom-color: #334155;
  }

  td {
    color: #e2e8f0;
    border-bottom-color: #334155;
  }

  tbody tr:hover {
    background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
  }

  .empty {
    color: #94a3b8;
  }

  .inline-list li {
    background: rgba(59, 130, 246, 0.2);
    color: #60a5fa;
  }

  .error {
    background: linear-gradient(135deg, #7f1d1d 0%, #991b1b 100%);
    color: #fecaca;
    border-color: #991b1b;
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

  .composition-form {
    grid-template-columns: 1fr;
  }

  .table-container {
    overflow: auto;
  }

  th, td {
    padding: 0.75rem;
    font-size: 0.8rem;
  }
}
</style>
