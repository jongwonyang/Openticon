FROM node:23-alpine

WORKDIR /app

COPY package*.json ./

RUN npm install

# dist 폴더에서 실행 파일 복사
COPY app.js ./app.js

# 이미지 업로드 디렉토리 생성
RUN mkdir -p static/upload/images

EXPOSE 8070

CMD ["node", "app.js"]
