# Start with PGAcloud Agent as base image.
FROM jluech/pga-cloud-agent:latest
MAINTAINER "Janik Luechinger janik.luechinger@uzh.ch"

# Expand with custom operator image.
FROM openjdk:14-alpine
MAINTAINER "Janik Luechinger janik.luechinger@uzh.ch"

COPY . /operator
WORKDIR /operator

# Insert agent container configuration file.
COPY custom-config.yml /pga/custom-config.yml

# Create and copy jar file for custom operator.
RUN jar cfmv FitnessEvaluation.jar Manifest.txt src/main/java/fitness
COPY FitnessEvaluation.jar /pga/FitnessEvaluation.jar

# Manual image building
# docker build -t pga-cloud-fitness .
# docker tag pga-cloud-fitness jluech/pga-cloud-fitness
# docker push jluech/pga-cloud-fitness
