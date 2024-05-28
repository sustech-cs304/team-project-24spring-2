docker container rm ces
docker run --name ces -p 8888:88 \
--add-host host.docker.internal:host-gateway \
-e VITE_PROXY_DOCUMENTS="http://host.docker.internal:19000" \
-e VITE_PROXY_IMAGES="http://host.docker.internal:19000" \
-e VITE_PROXY_API="http://host.docker.internal:8080" \
-e CES_ADMIN_PORT=88 \
-e CES_SERVER_NAME="sustc.mark455.cn" \
-e VITE_API_BASE_URL="http://host.docker.internal" \
 ces-admin:v1.0