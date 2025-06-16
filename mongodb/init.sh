#!/bin/bash
set -e

if [ -z "$MONGODB_PASSWORD" ]; then
    echo "❌ MONGODB_PASSWORD not defined"
    exit 1
fi

echo "📁 Ensuring required directories exist..."
mkdir -p /data/db /var/log
touch /var/log/mongodb.log
chown -R mongodb:mongodb /data/db /var/log/mongodb.log

CREATE_USER_JS="
if (!db.getUser('user')) {
    db.createUser({
        user: 'user',
        pwd: '$MONGODB_PASSWORD',
        roles: [{ role: 'readWrite', db: 'metrics' }]
    });
}
"

echo "🔁 Starting MongoDB without auth..."
gosu mongodb mongod --fork --logpath /var/log/mongodb.log --dbpath /data/db --bind_ip_all

echo "⏳ Waiting for MongoDB to initialize..."
until mongosh piggymetrics --eval "$CREATE_USER_JS"; do
    echo "🔄 Retrying user creation..."
    sleep 3
done

if [ -n "$INIT_DUMP" ]; then
    echo "📦 Running init dump: $INIT_DUMP"
    until mongosh piggymetrics -u user -p "$MONGODB_PASSWORD" "$INIT_DUMP"; do
        echo "🔁 Retrying dump..."
        sleep 3
    done
fi

echo "🛑 Stopping MongoDB to enable auth..."
mongod --shutdown || echo "⚠️ Could not shut down cleanly"

sleep 2

echo "🔒 Starting MongoDB with authentication"
exec gosu mongodb /usr/local/bin/docker-entrypoint.sh --auth "$@"
