ARG NODE_VERSION=20-alpine
ARG NGINX_VERSION=1.25-alpine

FROM node:${NODE_VERSION} AS builder

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm ci

COPY . .

RUN npm run build --prod

FROM nginx:${NGINX_VERSION} AS runner

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/dist/nomad-client /usr/share/nginx/html


CMD ["nginx", "-g", "daemon off;"]
