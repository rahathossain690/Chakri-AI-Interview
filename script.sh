#!/bin/bash
set -e # exit on error

# ./script.sh --dev
# ./script.sh --prod
# ./script.sh --stop

dev=0
prod=0
stop=0

while [[ $# -gt 0 ]]; do
  case "$1" in
    --dev)
      dev=1
      prod=0
      stop=0
      shift ;;
    --prod)
      dev=0
      prod=1
      stop=0
      shift ;;
    --stop)
      dev=0
      prod=0
      stop=1
      shift ;;
    *)
      echo "Unknown argument: $1"
      exit 1
  esac
done

function create_sample_env() {
    echo "🟢 creating sample env"
    find . -type f -name ".env" -exec bash -c '
        dir=$(dirname "$1")
        if [ -f "$1" ]; then
            sed "s/=.*$/=/" "$1" > "$dir/sample.env"
            echo "🟢 Created sample.env in $dir"
        fi
        ' bash {} \;
}

function check_env_files_sanity(){
    echo "🟢 Checking .env files for sanity"
    find . -type f -name ".env" -print0 | while IFS= read -r -d $'\0' env_file; do
        dir=$(dirname "$env_file")
        sample_file="$dir/sample.env"

        if [ ! -f "$sample_file" ]; then
            echo "🔴 sample.env not found in $dir. Skipping."
            exit 1
        fi

        env_keys=$(awk -F= '{print $1}' "$env_file" | sort)
        sample_keys=$(awk -F= '{print $1}' "$sample_file" | sort)

        if [[ "$env_keys" != "$sample_keys" ]]; then
            echo "🔴 Keys in .env and sample.env do not match in $dir. Stopping."
            exit 1
        fi

    done
}

function create_sample_properties() {
    echo "🟢 creating sample properties"
  find . -type f -name "*.properties" ! -name "sample-*.properties" -print0 | while IFS= read -r -d $'\0' prop_file; do
    dir=$(dirname "$prop_file")
    filename=$(basename "$prop_file")
    first_part="${filename%.properties}"
    sample_file="$dir/sample-$filename"

      sed 's/=.*$/=/' "$prop_file" > "$sample_file"
      echo "🟢 Created $sample_file"
  done
}

function check_for_properties_sanity() {
    echo "🟢 Checking .properties files for sanity"
  find . -type f -name "*.properties" ! -name "sample-*.properties" -print0 | while IFS= read -r -d $'\0' prop_file; do
    dir=$(dirname "$prop_file")
    filename=$(basename "$prop_file")
    first_part="${filename%.properties}"
    sample_file="$dir/sample-$filename"

    if [ ! -f "$sample_file" ]; then
      echo "🔴 sample-$filename not found in $dir. Exiting."
      exit 1
    fi

    prop_keys=$(sed 's/=.*//' "$prop_file" | sort)
    sample_keys=$(sed 's/=.*//' "$sample_file" | sort)

    if [[ "$prop_keys" != "$sample_keys" ]]; then
      echo "🔴 Keys in $filename and sample-$filename do not match in $dir. Exiting."
      exit 1
    fi

  done
}

function check_for_docker(){
    echo "🟢 Checking for docker and docker-compose"

    if ! command -v docker &> /dev/null; then
        echo "🔴 Docker is not installed. Please install docker and try again."
        exit 1
    fi

    if ! command -v docker-compose &> /dev/null; then
        echo "🔴 Docker-compose is not installed. Please install docker-compose and try again."
        exit 1
    fi
}

run_docker_containers() {
    echo "🟢 Running docker container"
    nohup docker-compose -f docker-compose.yml up & > debug.log
}

stop_docker_containers() {
    echo "🟢 Stopping docker container"
    docker-compose -f docker-compose.yml down
}

if [[ $dev -eq 1 ]]; then
    echo "🟢 Starting the app in development mode"
    check_for_docker
    create_sample_env
    create_sample_properties
    run_docker_containers

    echo "🟢 Deploy log can be found at debug.log"

elif [[ $prod -eq 1 ]]; then
    echo "🟢 Starting the app in production mode"
    check_for_docker
    check_env_files_sanity
    check_for_properties_sanity
    run_docker_containers

    echo "🟢 Deploy log can be found at debug.log"

elif [[ $stop -eq 1 ]]; then
    echo "🟢 Stopping the app"
    check_for_docker
    stop_docker_containers

else
    echo "🔴 No action specified"
    exit 1
fi