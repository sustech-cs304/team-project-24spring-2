docker build -f ./DOCKERFILE . -t ces-admin:v1.0 \
    --build-arg HTTP_PROXY=http://127.0.0.1:7899 \
	--build-arg HTTPS_PROXY=http://127.0.0.1:7899