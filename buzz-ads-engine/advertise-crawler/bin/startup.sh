#!/bin/sh

CLASSPATH=conf:advertise-crawler.jar
for f in lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f
done
java -server -Dfile.encoding=UTF-8 -Xmx2G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+CMSIncrementalPacing -XX:CMSIncrementalDutyCycleMin=0 -XX:CMSIncrementalDutyCycle=10 -XX:MaxNewSize=256M -XX:MaxPermSize=256M -cp $CLASSPATH com.buzzinate.advertise.crawl.Crawler > /dev/null 2>&1 &
