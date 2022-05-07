# ELK configuration

Tutorial project with full ELK configuration using simple REST API for generating logs by log4j library.

## Requirements

Required JDK version: 11

## Running application

This application can be started by running 'ElkApplication.class' without any additional startup configuration.
Application will be available on 'localhost:8080' port (default for Tomcat). This can be changed in 'application.properties' file by changing 'server.port' property.

## Log4j configuration 

In file 'log4j2.yml' there is example configuration of three different appenders.
All appenders use the same log pattern (defined by 'PatternLayout.pattern' property).
All appenders have also unique name (defined by 'name' property)

Example logs has structure:
"[%level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"

%level describe log severity (info, warn, error, trace, debug)
%d{yyyy-MM-dd HH:mm:ss.SSS} - define timestamp formatting
%t insert thread identificator
%c insert logger instance name
%msg insert actual message logged by application

To see all possible pattern configuration check official Log4j2 documentation.

### Console appender

This appender can be used to display logs in console (f.e. build-in console in any IDE or on system command line from which program is started).

### RollingFile appender

This appender will write logs into files in the location defined by `fileName` property. When the file reach the defined by 'Policies.SizeBasedTriggeringPolicy.size' property size (10MB), then file will be automaticlly compressed and logs will be added into a new file. 

### Syslog appender

This appender is used in this example by default. By this configuration a new Socket is created when application starts on defined host and port. All logs will be send to that Socket by TCP protocol.

### Appender usage

Following properties describes how and what should be logged:

Root.level value `all` will log all logs regardless their severity 
Root.AppenderRef.ref value of this property refers to appender's name which have to be used.

## REST API

In this application there is prepared one endpoint which execute metod generating some logs of different severities.

Send 'GET' request to '/api/logs/generate'. 
Can be done f.e. by opening 'http://localhost:8080/api/logs/generate' page in any browser.

# ELK

## Preparation

Create root directory, f.e. `C:/ELK`

## Elasticsearch

### Source
Download Elasticsearch from following page: https://www.elastic.co/downloads/elasticsearch and extract sources into created root directory

### Installation
Run specific for you OS script 'elasticsearch' from '/bin' dir. 

## Logstash

### Source
Download Logstash from following page: https://www.elastic.co/downloads/logstash and extract sources

## Kibana

### Source
Download Kibana from following page: https://www.elastic.co/downloads/kibana and extract sources

### Installation
