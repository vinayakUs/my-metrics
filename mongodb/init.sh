#!/bin/bash
set -e


: "${MONGODB_PASSWORD:=password}"
echo "🔑 Using MongoDB password: $MONGODB_PASSWORD"


echo "📁 Ensuring required directories exist..."
mkdir -p /data/db /var/log
touch /var/log/mongodb.log
chown -R mongodb:mongodb /data/db /var/log/mongodb.log

CREATE_USER_JS='
if (!db.getUser("user")) {
    db.createUser({
        user: "user",
        pwd: "'"$MONGODB_PASSWORD"'",
        roles: [{ role: "readWrite", db: "metrics" }]
    });
}
'

echo "🔁 Starting MongoDB without auth..."
gosu mongodb mongod --fork --logpath /var/log/mongodb.log --dbpath /data/db --bind_ip_all

echo "🔍 Waiting for MongoDB to accept connections..."
until mongosh metrics --eval "db.runCommand({ ping: 1 })" >/dev/null 2>&1; do
  echo "⏳ Mongo not ready, retrying..."
  sleep 2
done
echo "✅ MongoDB is up"


echo "🔍 Verifying user creation...1"
mongosh metrics --eval 'db.getUsers()' | tee /var/log/mongo_user_check.log

echo "⏳ Waiting for MongoDB to initialize..."
until mongosh metrics --eval "$CREATE_USER_JS"; do
    echo "🔄 Retrying user creation..."
    sleep 3
done


echo "🔍 Verifying user creation...2"
mongosh metrics --eval 'db.getUsers()' | tee /var/log/mongo_user_check.log


if [ -n "$INIT_DUMP" ]; then
    echo "📦 Running init dump: $INIT_DUMP"
    until mongosh metrics -u user -p "$MONGODB_PASSWORD" "$INIT_DUMP"; do
        echo "🔁 Retrying dump..."
        sleep 3
    done
fi

echo "🛑 Stopping MongoDB to enable auth..."
#mongod --shutdown || echo "⚠️ Could not shut down cleanly"
gosu mongodb mongod --dbpath /data/db --shutdown || echo "⚠️ Could not shut down cleanly"

sleep 2

echo "🔒 Starting MongoDB with authentication"
exec gosu mongodb /usr/local/bin/docker-entrypoint.sh --auth "$@"
