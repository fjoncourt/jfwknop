__To run the application, download the target directory and run java -jar com.cipherdyne.jfwknop-x.y.z.jar. There is no need to build the application to launch the user interface unless you did somes changes to the source code.__

# Features
* Easy access to gnupg directories and ability to import, export, remove and even create GPG keys
* Random password generation
* Base64 encoding from clear passwords
* Fwknop client binary selection
* Ssh export to remote server
* Quick access to last configurations
* Internationalization support - English and French available - More can be easily added
* Fwknopd access.conf generation
* Periodic knocks

# Build

The application is a JAVA program and is built around maven that provides the ability to download dependencies from a central repository without the need to install them on the target system manually.

The build dependencies are:

* Java - JDK 1.8
* Maven


The below steps are performed on a Debian system

First of all, install the build dependencies:

```
 # apt-get install default-jdk maven
```

Then build the application from the root directory:

```
$ mvn clean install
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building Fwknop User Interface 1.0.5
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ com.cipherdyne.jfwknop ---
[INFO] Deleting /home/franck/Documents/java/fwknop/github/target
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ com.cipherdyne.jfwknop ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 29 resources
[INFO] 
[INFO] --- maven-compiler-plugin:2.4:compile (default-compile) @ com.cipherdyne.jfwknop ---
[INFO] Compiling 31 source files to /home/franck/Documents/java/fwknop/github/target/classes
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ com.cipherdyne.jfwknop ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 0 resource
[INFO] 
[INFO] --- maven-compiler-plugin:2.4:testCompile (default-testCompile) @ com.cipherdyne.jfwknop ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-surefire-plugin:2.17:test (default-test) @ com.cipherdyne.jfwknop ---
[INFO] 
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ com.cipherdyne.jfwknop ---
[INFO] Building jar: /home/franck/Documents/java/fwknop/github/target/com.cipherdyne.jfwknop-1.0.5.jar
[INFO] 
[INFO] --- maven-install-plugin:2.5.2:install (default-install) @ com.cipherdyne.jfwknop ---
[INFO] Installing /home/franck/Documents/java/fwknop/github/target/com.cipherdyne.jfwknop-1.0.5.jar to /home/franck/.m2/repository/com/cipherdyne/jfwknop/com.cipherdyne.jfwknop/1.0.5/com.cipherdyne.jfwknop-1.0.5.jar
[INFO] Installing /home/franck/Documents/java/fwknop/github/pom.xml to /home/franck/.m2/repository/com/cipherdyne/jfwknop/com.cipherdyne.jfwknop/1.0.5/com.cipherdyne.jfwknop-1.0.5.pom
[INFO] 
[INFO] --- maven-dependency-plugin:2.8:copy-dependencies (default) @ com.cipherdyne.jfwknop ---
[INFO] Copying bcprov-jdk15on-1.54.jar to /home/franck/Documents/java/fwknop/github/target/lib/bcprov-jdk15on-1.54.jar
[INFO] Copying log4j-1.2.17.jar to /home/franck/Documents/java/fwknop/github/target/lib/log4j-1.2.17.jar
[INFO] Copying miglayout-3.7.4.jar to /home/franck/Documents/java/fwknop/github/target/lib/miglayout-3.7.4.jar
[INFO] Copying bcpg-jdk15on-1.54.jar to /home/franck/Documents/java/fwknop/github/target/lib/bcpg-jdk15on-1.54.jar
[INFO] Copying commons-lang3-3.1.jar to /home/franck/Documents/java/fwknop/github/target/lib/commons-lang3-3.1.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 9.437 s
[INFO] Finished at: 2016-05-21T12:10:30+02:00
[INFO] Final Memory: 20M/156M
[INFO] ------------------------------------------------------------------------
```
Go to the target directory. You will find:

* The application: com.cipherdyne.jfwknop-x.y.z.jar
* all its dependencies in the lib directory


# Run

Once the application is built go to the target directory and run:
```
java -jar com.cipherdyne.jfwknop-1.0.0.jar
```

# Multi-platform
The application, can be run on windows, Linux or any other OS. The only requirements is to have JRE 1.8 isntalled on the target computer.

Once built, you can deploy the application wherever you want by copying the jar file, its configuration file and the lib directory.
