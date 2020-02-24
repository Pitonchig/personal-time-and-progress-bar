FROM jenkins/jenkins:latest

USER root

RUN apt-get update \
    && apt-get -y install \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg2 \
        software-properties-common

# docker repos
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -\
    && add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"\
    && apt-get update

# docker
RUN apt-get -y install docker-ce docker-compose

# give jenkins docker rights
RUN usermod -aG docker jenkins

USER jenkins
