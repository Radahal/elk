# ELK configuration

Tutorial project with full `ELK` configuration using simple REST API for generating logs by `Log4j2` lib.

## Requirements

Required JDK version: `11`

## Running application

This application can be started by running `ElkApplication.class` without any additional startup configuration.

Application will be available on `localhost:8080` port (default for Tomcat). 

This can be changed in `application.properties` file by modifing `server.port` property.

## Log4j configuration 

In file `log4j2.yml` there is example configuration of three different appenders.
All appenders use the same log pattern (defined by `PatternLayout.pattern` property).
All appenders have also unique name (defined by `name` property)

Example log has structure:

```
[INFO ] 2022-05-05 21:37:52.573 [http-nio-8080-exec-1] LoggingService - Info log 56
```

`Pattern` used to achive that formatting:

```
"[%level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"
```

- `%level` describe log severity (info, warn, error, trace, debug)
- `%d{yyyy-MM-dd HH:mm:ss.SSS}` - define timestamp formatting
- `%t` insert thread identificator
- `%c` insert logger instance name
- `%msg` insert actual message logged by application

To see all possible pattern configuration check official [Log4j2 documentation](https://logging.apache.org/log4j/2.x/manual/layouts.html).

### Sample configuration for Logstash
In sources of Logstash there is `\config\log4j2.properties` file with different appender configurations dedicated for Logstash.

### Example appenders

#### Console

This appender can be used to display logs on any console (f.e. build-in console in IDE or on system command line from which program is launched).

#### RollingFile

This appender will write logs into files in the location defined by `fileName` property. When the file reach the defined by `Policies.SizeBasedTriggeringPolicy.size` property size (10MB), then file will be automaticlly compressed and logs will be added into a new file. 

#### Syslog

This appender is used in this example by default. By this configuration a new Socket is created when application starts on defined host and port (by properties `host` and `port`. All logs will be send to that Socket by TCP protocol (can be changed by modifining `protocol` property).

### Appender usage

Following properties describes how and what should be logged:

- `Root.level` property with value `all` will print all logs regardless their severity 
- `Root.AppenderRef.ref` property value refers to appender's name which will be used.

## REST API

In this application there is prepared one endpoint which execute method responsible for generating some logs of different severities.

Send `GET` request to `/api/logs/generate`. 
Can be done f.e. by opening `http://localhost:8080/api/logs/generate` page in any browser (only when application is running locally).

## ELK

## Preparation
Create root directory, f.e. `C:\ELK`

## Elasticsearch

### Source
Download [Elasticsearch](https://www.elastic.co/downloads/elasticsearch) and extract sources into created root directory

### Installation
Run specific for you OS script `elasticsearch` from `\bin` dir. 

When installed it will be available on `9200` port. 

#### Enrollment token
During startup Elasticsearch uses default configuration which generate Enrollment token for Kibana valid for 30 minutes.

For more detailed configuration [see original documentation](https://www.elastic.co/guide/en/elasticsearch/reference/8.2/starting-elasticsearch.html).

## Logstash

### Source
Download [Logstash](https://www.elastic.co/downloads/logstash) and extract sources into created root directory

### Configuration
Logstash doesn't required installation like Kibana or Elasticsearch. It's ready to run but require dedicated configuration file.

#### Sample configuration
When extracted you can find `\config` directory with `logstash-sample.conf` file. 
This example configuration set as input stream from Beats plugin. Usually it is used when logs are stored in files and that plugin can listen on that file for new content and send it to Logstash using [Filebeat](https://www.elastic.co/guide/en/beats/filebeat/8.2/configuring-howto-filebeat.html)

#### Configuration

In sources you can find file `logstash.conf` which contains our configuration.

Input is declared as `TCP` input from `host` and `port` set-up in `log4j2.yml` configuration.
```
input {
  tcp {
    host => "127.0.0.1"
    port => 4560
  }
}
```

Filter is using [Grok plugin](https://www.elastic.co/guide/en/logstash/current/plugins-filters-grok.html) with `log4j2 pattern` but written in Grok syntax. It allows Logstash to parse input into desire json data.

To parse `pattern` into `Grok` synthax there is a [GrokConstructor tool](https://grokconstructor.appspot.com/) 
```
filter {
    grok {
        match => { "message" => "\[%{LOGLEVEL:loglevel}\] (?<timestamp>%{YEAR}-%{MONTHNUM2}-%{MONTHDAY} %{HOUR}:%{MINUTE}:%{SECOND}.%{NONNEGINT}) \[%{NOTSPACE:thread}\] (?<logger>[A-Za-z0-9$_.]+) - %{GREEDYDATA:log_message}$"}
    }
}
```

Output is declared as `elasticsearch` running locally. Our configuration does not include ssh configuration so it also contains credential for elasticsearch user.
```
output {
  elasticsearch {
    user => logstash_internal
    password => logstash
    hosts => ["127.0.0.1:9200"]
    index => "logstash-%{+YYYY.MM.dd}"
  }
}
```

**Note:** as `index` it is common to start with name of service which create logs. It is goodto create an pattern or that property becuase of further usage that values in Kibana.

#### Custom configuration

For more specific configuration [see original documentation](https://www.elastic.co/guide/en/logstash/current/config-examples.html).

## Kibana

### Source
Download [Kibana](https://www.elastic.co/downloads/kibana) and extract sources into created root directory

### Installation
Run specific for you OS script `kibana` from `\bin` dir. 

When installed it will be available on address printed in your console. It's important to use that one because it uses enrollment token generated by Elasticsearch.

#### Overriding default configuration

For more specific configuration [see original documentation](https://www.elastic.co/guide/en/kibana/current/settings.html).

### Data View
Once Kibana is started it is time to create `Data View` which aggregate some indices with data. It is possible in `Data Views` panel by using `Create data view` option.

**Data view creation**

![image](https://user-images.githubusercontent.com/6892780/167270310-3d5f6ed0-37a7-49cb-9b43-9e5c6c9bbc3b.png)

Field `name` requires regular expression which match your desire indices. 

Following configuration from logstash indices in elastic have pattern: `logstash-%{+YYYY.MM.dd}` so name which will be matching all of them is f.e. `logstash-*`.
As `timestamp` select `@Timestamp`.

That Data View allows to look into logs stored in Elasticsearch.

### Discover
Once Data View is created it is possible to select it in `Discover` panel.

**Discover panel view**

![image](https://user-images.githubusercontent.com/6892780/167270423-33ea074b-5d3a-49f2-9fa5-6726c12e5f8d.png)

This tool allows you to display, filter, analyze all logs produced by application.

F.e. it is possible to filter all logs by specific value of any field. This fields were defined in Logstash filter.

**Filter creation**

![image](https://user-images.githubusercontent.com/6892780/167270505-f360d53c-534f-4673-b4e4-d48500780923.png)

**Filtered result**

![image](https://user-images.githubusercontent.com/6892780/167270530-6a48091c-2b6d-41f8-ac90-5f4933508c99.png)

### Dashboard

Dashboard allows to create advanced control pannel for analisis of logs content.

#### Visualization

It is possible to display on dashboard multiple charts, lists, views by `Create new visualization` option.

##### Example chart

By chosing `Aggregation based` it is possible to create `Vertical Bar`. Configuration wizard has 4 steps to choose:

- Choose a source: select created Data View
- Metrics/ Y-axis: select Count as Aggregation method
- Define Buckets/ X-axis: select `Date Histogram` as `Aggregation` method, `@timestamp` as `Field`, `Day` as `Minimum interval`
- Split series: Select `Terms` as `Sub Aggregation`, `loglevel.keyword` as `Field`, `Metric: Count` as Order By, and insert `5` as `Size`

This visualization config creates for each day other bar chart representing proportion between log severity(level).

**Log level bar chart**

![image](https://user-images.githubusercontent.com/6892780/167270828-c7add71d-1f51-4ac5-8d11-724f31458412.png)

#### Custom dashboard

For more specific configuration [see original documentation](https://www.elastic.co/guide/en/kibana/current/dashboard.html).

#### Alerts

It is possible to configure alerts (f.e. email send to administrator) when suspicious event occurs. 

For tutorials [see original documentation](https://www.elastic.co/guide/en/kibana/current/alerting-getting-started.html)
