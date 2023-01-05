FROM adoptopenjdk/openjdk16:ubi
VOLUME /tmp
COPY /build/libs/codepied.jar ./
COPY /config/*.yml ./config/
ENTRYPOINT ["java", "-jar", "/codepied.jar"]