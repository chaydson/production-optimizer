export interface RawMaterialRequest {
  code: string;
  name: string;
  stockQuantity: number;
}

export interface RawMaterialResponse {
  id: string;
  code: string;
  name: string;
  stockQuantity: number;
}

export interface CompositionRequest {
  rawMaterialId: string;
  quantity: number;
}

export interface CompositionResponse {
  rawMaterialId: string;
  rawMaterialCode?: string;
  rawMaterialName?: string;
  quantity: number;
}

export interface ProductRequest {
  code: string;
  name: string;
  price: number;
  compositions: CompositionRequest[];
}

export interface ProductResponse {
  id: string;
  code: string;
  name: string;
  price: number;
  compositions: CompositionResponse[];
}

export interface PlanItem {
  productCode: string;
  productName: string;
  quantityToProduce: number;
  expectedRevenue: number;
}

export interface ProductionPlan {
  items: PlanItem[];
  totalExpectedRevenue: number;
}
