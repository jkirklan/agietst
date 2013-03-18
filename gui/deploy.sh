#!/bin/bash

EAP_HOME=../../servers/jboss-eap-6.0

# Start the EAP server
cwd=`pwd`
cd $EAP_HOME/bin
./standalone.sh > $EAP_HOME/bin/console.out &
cd $cwd

# Build the packages
mvn clean package

# Stop the EAP instances
cd $EAP_HOME/bin
./jboss-cli.sh -c --command=:shutdown > $EAP_HOME/bin/cli-console.out
cd $cwd

# Copy the deployments files to EAP
cp plane-tracker/target/plane-tracker-0.0.2.war $EAP_HOME/standalone/deployments/plane-tracker.war
cp services/target/agie-services-0.0.2.war $EAP_HOME/standalone/deployments/agie-services.war

