FROM mongo:7

# Copy init script and dump
COPY init.sh /init.sh
COPY dump /dump

RUN chmod +x /init.sh && \
    apt-get update && \
    apt-get install -y --no-install-recommends gosu dos2unix && \
    mkdir -p /data/db /var/log && \
    chown -R mongodb:mongodb /data/db /var/log && \
    dos2unix /init.sh && \
    apt-get purge -y dos2unix && apt-get autoremove -y && apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

ENTRYPOINT ["/init.sh"]
