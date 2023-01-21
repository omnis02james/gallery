#!/bin/bash

mvn -e -q clean
mvn -e -q compile
mvn -e -q -Dprism.order=sw exec:java \
    -Dexec.cleanupDaemonThreads=false \
    -Dexec.mainClass="cs1302.gallery.GalleryDriver"
