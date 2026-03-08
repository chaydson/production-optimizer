# Production Optimizer

Sistema para **otimização de produção** e **gestão de produtos e matérias‑primas**.

- **Backend:** Java + Quarkus (API REST) — pasta `backend/`
- **Frontend:** Vue 3 + Vite + TypeScript — pasta `frontend/`

## Funcionalidades

- **Otimização de Produção:** calcula e exibe um plano de produção otimizado
- **Cadastro de Produtos:** criar e listar produtos com suas composições
- **Cadastro de Matérias‑primas:** criar e listar matérias‑primas com quantidades em estoque

## Estrutura do repositório

- `backend/` — API (Quarkus + Maven)
- `frontend/` — aplicação web (Vue + Vite)

## API (Backend)

### Requisitos
- Java 17
- Maven (ou usar o wrapper `./mvnw`)
- (Opcional) PostgreSQL (há dependência no projeto)

### Rodando em modo dev
```bash
cd backend
./mvnw quarkus:dev
```

A aplicação sobe (por padrão) em `http://localhost:8080`.

### Build / empacotamento
```bash
cd backend
./mvnw package
```

## Frontend

### Requisitos
- Node.js + npm

### Instalação
```bash
cd frontend
npm install
```

### Configuração de ambiente
Crie um arquivo `frontend/.env` (ou ajuste o existente) com:
```env
VITE_API_BASE_URL=http://localhost:8080
```

### Rodando em dev
```bash
cd frontend
npm run dev
```

### Build
```bash
cd frontend
npm run build
```

## Endpoints consumidos pelo Frontend

- `GET /api/production/optimize` — plano de produção otimizado
- `GET /api/products` — lista produtos
- `POST /api/products` — cria produto
- `GET /api/raw-materials` — lista matérias‑primas
- `POST /api/raw-materials` — cria matéria‑prima

## Como rodar tudo (local)

1. Inicie o backend:
   ```bash
   cd backend
   ./mvnw quarkus:dev
   ```
2. Configure o `VITE_API_BASE_URL` no `frontend/.env` apontando para o backend (ex.: `http://localhost:8080`)
3. Inicie o frontend:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
