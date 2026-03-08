import type {
  RawMaterialRequest,
  RawMaterialResponse,
  ProductRequest,
  ProductResponse,
  ProductionPlan
} from '../types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const error = await response.text();
    throw new Error(error || `HTTP error! status: ${response.status}`);
  }
  return response.json();
}

export const rawMaterialsAPI = {
  async create(data: RawMaterialRequest): Promise<RawMaterialResponse> {
    const response = await fetch(`${API_BASE_URL}/api/raw-materials`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    return handleResponse<RawMaterialResponse>(response);
  },

  async listAll(): Promise<RawMaterialResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/raw-materials`);
    return handleResponse<RawMaterialResponse[]>(response);
  },
};

export const productsAPI = {
  async create(data: ProductRequest): Promise<ProductResponse> {
    const response = await fetch(`${API_BASE_URL}/api/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    return handleResponse<ProductResponse>(response);
  },

  async listAll(): Promise<ProductResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/products`);
    return handleResponse<ProductResponse[]>(response);
  },
};

export const productionAPI = {
  async getOptimization(): Promise<ProductionPlan> {
    const response = await fetch(`${API_BASE_URL}/api/production/optimize`);
    return handleResponse<ProductionPlan>(response);
  },
};
