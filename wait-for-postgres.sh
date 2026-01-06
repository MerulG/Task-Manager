#!/bin/sh
set -e

host="$1"
shift
cmd="$@"

echo "Waiting for postgres at $host..."
until pg_isready -h "$host" -U "merul"; do
  echo "Postgres not ready yet..."
  sleep 2
done

echo "Postgres is ready! Starting Spring Boot..."
exec $cmd