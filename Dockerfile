FROM gradle:8.11.1-jdk17 AS backend-build

WORKDIR /app
COPY . .

RUN gradle :backend:bootJar --no-daemon

FROM node:22-alpine AS frontend-build

WORKDIR /app/frontend
COPY frontend/package.json frontend/package-lock.json* /app/frontend/
RUN npm install

COPY frontend/ /app/frontend/
RUN npm run build

FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache nodejs npm

WORKDIR /app

COPY --from=backend-build /app/backend/build/libs/*.jar app.jar
COPY --from=frontend-build /app/frontend/dist /app/frontend/dist
COPY --from=frontend-build /app/frontend/node_modules /app/frontend/node_modules
COPY --from=frontend-build /app/frontend/server /app/frontend/server
COPY --from=frontend-build /app/frontend/vite.config.js /app/frontend/vite.config.js
COPY --from=frontend-build /app/frontend/package.json /app/frontend/package.json

ENV NODE_ENV=production

EXPOSE ${PORT:-3000}

CMD java -jar app.jar & \
    cd /app/frontend && node server/index.js & \
    wait