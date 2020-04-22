FROM openjdk:11
ADD target/kick-scooter-payment.jar kick-scooter-payment.jar
ADD /home/sumo_credentials.txt /home/sumo_credentials.txt
RUN wget "https://collectors.sumologic.com/rest/download/linux/64" -O SumoCollector.sh && chmod +x SumoCollector.sh 
RUN ./SumoCollector.sh -q -varfile /home/sumo_credentials.txt -Vcollector.name=payment
ENTRYPOINT ["java","-jar","kick-scooter-payment.jar"]
