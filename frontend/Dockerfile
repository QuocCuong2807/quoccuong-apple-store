FROM node:20-alpine

WORKDIR /app

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép các tệp cấu hình yarn
COPY package.json yarn.lock ./
RUN yarn install

# Sao chép mã nguồn và xây dựng ứng dụng
COPY . .
RUN yarn build

# Cài đặt http-server để phục vụ ứng dụng ReactJS
RUN yarn global add http-server

# Cấu hình http-server để phục vụ các tệp tĩnh
CMD ["http-server", "build", "-p", "3000"]

# Mở cổng 3000
EXPOSE 3000