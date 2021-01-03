#!/usr/bin/env sh

if [ -z ${1+x} ]; then

  docker-compose -f docker-compose-local-build.yaml up

else

  echo "Force rebuild, recreate"

  ./java-build.sh

  ./dockerize.sh

  docker-compose -f docker-compose-local-build.yaml up --force-recreate

fi


