FROM openjdk:8-jre-alpine3.9
RUN apk update && apk upgrade && apk add ca-certificates && update-ca-certificates \
    && apk add --update tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && rm -rf /var/cache/apk/*
ENV TZ=Asia/Shanghai
VOLUME /tmp
COPY target/paladin-boot-1.0.jar /app.jar
EXPOSE 8900
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]