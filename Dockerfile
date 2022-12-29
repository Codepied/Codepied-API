FROM adoptopenjdk/openjdk16:ubi
VOLUME /tmp
COPY /build/libs/codepied.jar ./
ENTRYPOINT ["java", "-jar", "/codepied.jar"]