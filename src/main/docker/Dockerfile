FROM open-registry.going-link.com/zhenyun/javabase-slim:0.1.0
COPY app.jar /srm-source.jar
ENTRYPOINT exec java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap $JAVA_OPTS $APM_OPTS -jar /srm-source.jar