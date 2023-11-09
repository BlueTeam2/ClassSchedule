ARG NODE_VERSION=18
ARG FRONT_DIR_BASE=./frontend
ARG FRONT_WORK_DIR=/usr/src/app

FROM node:${NODE_VERSION}-alpine as frontend-base
ARG FRONT_WORK_DIR
WORKDIR ${FRONT_WORK_DIR}
ARG FRONT_DIR_BASE
RUN --mount=type=bind,source=${FRONT_DIR_BASE}/package.json,target=package.json \
    --mount=type=cache,target=/root/.npm \
    npm install && \
    mkdir -p node_modules/.cache && chmod -R 777 node_modules/.cache && \
    mkdir -p build && chmod -R 777 build
USER node
COPY ${FRONT_DIR_BASE} .
EXPOSE 3000

FROM frontend-base as frontend-dev
ENTRYPOINT [ "npm", "run", "start" ]

FROM frontend-base as frontend-prod
ENV REACT_APP_API_BASE_URL=/
RUN npm run build

FROM gradle:7.6.3-jdk11 as backend-build
WORKDIR /home/gradle
COPY --chown=gradle . .

FROM backend-build as backend-test
CMD gradle test

FROM backend-build as backend-build-dev
RUN gradle assemble

FROM backend-build as backend-build-prod
WORKDIR /home/gradle/src/main/webapp/
ARG FRONT_WORK_DIR
RUN rm -rf ./* && \
    mkdir -p ./WEB-INF/view/
COPY --from=frontend-prod ${FRONT_WORK_DIR}/build/ .
RUN mv *.* ./WEB-INF/view/
WORKDIR /home/gradle
RUN gradle assemble

FROM tomcat:9-jre11 as backend-tomcat-dev
WORKDIR /usr/local/tomcat/webapps
COPY --from=backend-build-dev /home/gradle/build/libs/class_schedule.war ./ROOT.war
EXPOSE 8080

FROM tomcat:9-jre11 as backend-tomcat-prod
WORKDIR /usr/local/tomcat/webapps
COPY --from=backend-build-prod /home/gradle/build/libs/class_schedule.war ./ROOT.war
EXPOSE 8080