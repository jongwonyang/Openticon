# 베이스 이미지 설정
FROM node:22 AS build

# 작업 디렉토리 생성
WORKDIR /app

# 패키지 파일 복사
COPY package*.json ./

# 의존성 설치
RUN npm install

# 소스 코드 복사
COPY . .

# 빌드 시점에 사용할 환경 변수 정의
ARG VITE_API_BASE_URL
ARG VITE_APP_URL

# 환경 변수 설정
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}
ENV VITE_APP_URL=${VITE_APP_URL}

# 애플리케이션 빌드
RUN npm run build

# Nginx를 사용하여 배포
FROM nginx:alpine

# 빌드된 파일 복사
COPY --from=build /app/dist /usr/share/nginx/html

# Nginx 설정 파일 복사 (필요한 경우)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Nginx 포트 노출
EXPOSE 80

# Nginx 시작
CMD ["nginx", "-g", "daemon off;"]
