FROM quay.io/opsmxpublic/ubifips:8.7
MAINTAINER sig-platform@spinnaker.io
COPY fiat-web/build/install/fiat /opt/fiat
RUN yum -y install java-17-openjdk-devel wget vim net-tools curl nettle
RUN yum -y update
RUN adduser spinnaker
RUN mkdir -p /opt/fiat/plugins
RUN chmod -R 777 /opt/fiat/plugins/
RUN chown -R spinnaker:spinnaker /opt/
USER spinnaker
CMD ["/opt/fiat/bin/fiat"]
