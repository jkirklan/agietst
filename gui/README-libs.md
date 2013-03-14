# Library Install
Installing dependent libraries that don't exist in public Maven repositories

## Tasks
1. Copy qpid-client-0.18.jar and qpid-common-0.18.jar into this directory.
2. Run the following mvn commands:
- mvn install:install-file -DgroupId=com.redhat.mrgm -DartifactId=qpid-client -Dversion=0.18 -Dpackaging=jar -Dfile=qpid-client-0.18.jar
- mvn install:install-file -DgroupId=com.redhat.mrgm -DartifactId=qpid-common -Dversion=0.18 -Dpackaging=jar -Dfile=qpid-common-0.18.jar

