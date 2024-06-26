#!/bin/bash

# Check environment variables
if [ -z "${BIGDL_HOME}" ]; then
    echo "Please set BIGDL_HOME environment variable"
    exit 1
fi

if [ -z "${SPARK_HOME}" ]; then
    echo "Please set SPARK_HOME environment variable"
    exit 1
fi

#setup paths
export BIGDL_JAR=${BIGDL_HOME}/jars/*
export BIGDL_CONF=${BIGDL_HOME}/conf/spark-bigdl.conf
echo $BIGDL_JAR

# Check files
if [ ! -f ${BIGDL_CONF} ]; then
    echo "Cannot find ${BIGDL_CONF}"
    exit 1
fi

if [ ! -f $BIGDL_JAR ]; then
    echo "Cannot find $BIGDL_JAR"
    exit 1
fi

${SPARK_HOME}/bin/spark-shell \
  --properties-file ${BIGDL_CONF} \
  --jars ${BIGDL_JAR} \
  --conf spark.driver.extraClassPath=${BIGDL_JAR} \
  --conf spark.executor.extraClassPath=${BIGDL_JAR} \
  $*
