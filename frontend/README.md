# Production Management System

Frontend application for managing production optimization, products, and raw materials.

## Features

- **Production Optimization**: Calculate and view optimized production plans
- **Product Management**: Create and list products with compositions
- **Raw Material Management**: Create and list raw materials with stock quantities

## API Endpoints

This application consumes the following REST API endpoints:

- `GET /api/production/optimize` - Get optimized production plan
- `GET /api/products` - List all products
- `POST /api/products` - Create a new product
- `GET /api/raw-materials` - List all raw materials
- `POST /api/raw-materials` - Create a new raw material

## Setup

1. Install dependencies:
```bash
npm install
```

2. Configure the API URL in `.env`:
```
VITE_API_BASE_URL=http://localhost:8080
```

3. Start development server:
```bash
npm run dev
```

4. Build for production:
```bash
npm run build
```

## Usage

The application has three main sections:

1. **Production Plan**: View the optimized production plan with expected revenues
2. **Products**: Manage products and their compositions (raw material requirements)
3. **Raw Materials**: Manage raw materials and their stock quantities
