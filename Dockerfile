ARG BACK_WORK_DIR=/home/gradle

FROM gradle:7.6.3-jdk11 as backend-build
ARG BACK_WORK_DIR
WORKDIR ${BACK_WORK_DIR}
COPY --chown=gradle . .
RUN gradle assemble

FROM backend-build as backend-test
CMD gradle test

FROM tomcat:9-jre11 as backend-tomcat
ARG BACK_WORK_DIR
WORKDIR /usr/local/tomcat/webapps
COPY --from=backend-build ${BACK_WORK_DIR}/build/libs/class_schedule.war ./ROOT.war
EXPOSE 8080
