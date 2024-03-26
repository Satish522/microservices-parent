API_GATEWAY
===================
Docker command

docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak start-dev

Local keycloak server run the below command Go to under softwares -> keycloak folder -> bin -> open terminal ./kc.sh start-dev --hostname localhost --http-port 8181

URLS 
===========
Grafana :- http://localhost:3000/d/sOae4vCnk/spring-boot-statistics?orgId=1&refresh=5s&var-instance=inventory-service:8080&var-application=Inventory%20Service%20Application&var-hikaricp=HikariPool-1&var-memory_pool_heap=All&var-memory_pool_nonheap=All

Prometheus :- http://localhost:9090/graph?g0.expr=&g0.tab=1&g0.display_mode=lines&g0.show_exemplars=0&g0.range_input=1h

Discovery Server :- http://localhost:8761/

Zipkin :- http://localhost:9411/zipkin/

Keycloak :- http://localhost:8181/admin/master/console/#/spring-boot-microservices

Build Application
=====================
mvn compile jib:build 

Run All Application
=====================
docker-compose up -d

Stop All Application
=====================
docker-compose down 
