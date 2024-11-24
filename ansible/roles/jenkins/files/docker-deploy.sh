#!/bin/bash

LANGUAGE=()
REGISTRY=localhost:5000
DISPLAY_NAME=$(echo $(basename $(pwd)) | tr '[:upper:]' '[:lower:]')

if [[ -f Makefile ]]; then
	LANGUAGE+=("c")
fi
if [[ -f app/pom.xml ]]; then
	LANGUAGE+=("java")
fi
if [[ -f package.json ]]; then
	LANGUAGE+=("javascript")
fi
if [[ -f requirements.txt ]]; then
	LANGUAGE+=("python")
fi
if [[ $(find app -type f) == app/main.bf ]]; then
	LANGUAGE+=("befunge")
fi

if [[ ${#LANGUAGE[@]} == 0 ]]; then
	echo "Invalid project: no language matched."
	exit 1
fi
if [[ ${#LANGUAGE[@]} != 1 ]]; then
	echo "Invalid project: too many languages matched."
	exit 1
fi

image_name=whanos-${LANGUAGE[0]}-$DISPLAY_NAME
echo "Building $image_name"

if [[ -f Dockerfile ]]; then
	docker build . -t $image_name || exit 1
else
	docker build . \
		-f /opt/registry/${LANGUAGE[0]}/Dockerfile.standalone \
		-t $image_name || exit 1
fi

docker tag $image_name $REGISTRY/$image_name || exit 1
docker push $REGISTRY/$image_name || exit 1
docker pull $REGISTRY/$image_name || exit 1
docker rmi $image_name || exit 1
