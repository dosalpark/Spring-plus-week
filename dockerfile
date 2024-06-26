# openjdk 경량화 버전 사용
FROM openjdk:17-alpine

# 작업디렉토리로 이동
COPY ./build/libs/food-thought-0.0.1-SNAPSHOT.jar /application/app.jar

# 작업디렉토리
WORKDIR /application/

# JAR 파일 실행
CMD ["nohup", "java", "-jar", "app.jar"]
