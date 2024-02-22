API_GATEWAY
===================
Docker command

docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak start-dev

Local keycloak server run the below command Go to under softwares -> keycloak folder -> bin -> open terminal ./kc.sh start-dev --hostname localhost --http-port 8181
