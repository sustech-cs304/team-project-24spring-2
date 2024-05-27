#!/usr/bin/env sh
set -eu
envsubst '${VITE_PROXY_API} ${VITE_PROXY_IMAGES} ${VITE_PROXY_DOCUMENTS} ${CES_ADMIN_PORT} ${CES_SERVER_NAME}' < /etc/nginx/conf.d/nginx.conf.template > /etc/nginx/conf.d/nginx.conf
exec "$@"
