# Stage 1: Build ứng dụng từ mã nguồn bằng Gradle
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copy các file cấu hình Gradle trước để tận dụng cache của Docker
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Sửa lỗi phân quyền nếu chạy trên môi trường Linux (VPS)
RUN chmod +x gradlew

# Copy toàn bộ mã nguồn vào container
COPY src ./src

# Tiến hành build file JAR (bỏ qua chạy thử test để quá trình deploy nhanh hơn)
RUN ./gradlew bootJar -x test

# Stage 2: Tạo môi trường chạy ứng dụng gọn nhẹ
FROM amazoncorretto:17-alpine
WORKDIR /app

# Copy file JAR đã build từ Stage 1 sang Stage 2
COPY --from=build /app/build/libs/*.jar app.jar

# Khai báo port ứng dụng sẽ chạy bên trong container
EXPOSE 8080

# Lệnh kích hoạt ứng dụng Spring Boot
ENTRYPOINT ["java", "-Duser.timezone=Asia/Ho_Chi_Minh", "-jar", "app.jar"]