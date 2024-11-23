FROM jenkins/jenkins:lts

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
WORKDIR /var/whanos

COPY plugins.txt /var/whanos/plugins.txt
RUN jenkins-plugin-cli -f /var/whanos/plugins.txt

COPY my_whanos.yml /var/jenkins_home/casc_configs/my_whanos.yml
COPY job_dsl.groovy /usr/share/jenkins/ref/init.groovy.d/job_dsl.groovy

COPY . /var/whanos

USER root
RUN apt-get update && apt-get install -y make gcc libcriterion-dev gcovr
USER jenkins
