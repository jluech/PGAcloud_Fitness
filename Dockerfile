# Define custom operator image.
FROM openjdk:14-alpine
MAINTAINER "Janik Luechinger janik.luechinger@uzh.ch"

# Copy contents to container.
COPY . /operator
WORKDIR /operator

# Start with PGAcloud Agent as base image.
FROM jluech/pga-cloud-agent:latest
MAINTAINER "Janik Luechinger janik.luechinger@uzh.ch"

# Install java in python image.
# https://www.linuxuprising.com/2020/03/how-to-install-oracle-java-14-jdk14-on.html
RUN apt-get update && \
    apt-get -y upgrade && \
    echo oracle-java14-installer shared/accepted-oracle-license-v1-2 select true | /usr/bin/debconf-set-selections && \
    echo "deb http://ppa.launchpad.net/linuxuprising/java/ubuntu focal main" | tee /etc/apt/sources.list.d/linuxuprising-java.list && \
    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 73C3DB2A && \
    apt update && \
    apt-get -y upgrade && \
    apt install -y oracle-java14-installer

RUN java --version

# Insert agent container configuration file.
# https://duo.com/labs/tech-notes/multi-stage-builds-with-docker
COPY --from=0 /operator/custom-config.yml /pga/custom-config.yml

# Create jar file from custom operator in PGA agent.
COPY --from=0 /operator/main /pga/main
RUN javac /pga/main/fitness/*.java && \
    cd /pga/main && \
    jar cfe FitnessEvaluation.jar fitness.Main fitness

# Manual image building
# docker build -t pga-cloud-fitness .
# docker tag pga-cloud-fitness jluech/pga-cloud-fitness
# docker push jluech/pga-cloud-fitness
