resolvers ++= Seq(
  "buzzinate" at "http://58.83.175.30:8081/nexus/content/groups/public",
  "java m2" at "http://download.java.net/maven/2",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Codahale" at "http://repo.codahale.com",
  "maven local" at "file://C:/develop/apache-maven-3.0.5/repo",
  Resolver.url("sbt-plugin-releases", 
  	new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
)

organization := "com.buzzinate"

name := "buzz-ads-adx"

version := "0.1.0-SNAPSHOT"

javacOptions ++= Seq("-encoding", "UTF-8")

libraryDependencies ++= Seq(
  //////////////////////////////////////////////////////////////////////
  // Web server
  "io.netty" % "netty" % "3.5.9.Final",
  "net.databinder" %% "unfiltered-filter" % "0.6.2",
  "net.databinder" %% "unfiltered-jetty" % "0.6.2",
  "net.databinder" %% "unfiltered-netty-server" % "0.6.2",
  "net.databinder" %% "dispatch-nio" % "0.8.8",
  "net.databinder" %% "dispatch-http" % "0.8.8",
  "net.databinder" %% "unfiltered-uploads" % "0.6.2",
  "net.databinder" %% "unfiltered-filter-uploads" % "0.6.2",
  "org.fusesource.scalate" % "scalate-core" % "1.5.3",
  //////////////////////////////////////////////////////////////////////
  // Utils
  "org.apache.httpcomponents" % "httpcore" % "4.2",
  "org.apache.httpcomponents" % "httpclient" % "4.2",
  "commons-lang" % "commons-lang" % "2.5",
  "commons-pool" % "commons-pool" % "1.6",
  "commons-codec" % "commons-codec" % "1.6",
  "commons-beanutils" % "commons-beanutils" % "1.8.3",
  "org.safehaus.jug" % "jug" % "2.0.0" classifier "lgpl",
  "com.jolbox" % "bonecp" % "0.7.1.RELEASE",
  "com.typesafe.akka" % "akka-actor" % "2.0.4",
  "org.jsoup" % "jsoup" % "1.6.3",
  "com.googlecode.juniversalchardet" % "juniversalchardet" % "1.0.3",
  "com.codahale" %% "jerkson" % "0.5.0",
  libraryDependencies += "org.jsoup" % "jsoup" % "1.7.1",
  //////////////////////////////////////////////////////////////////////
  // Logging
  "org.slf4j" % "slf4j-api" % "1.7.2",
  "ch.qos.logback" % "logback-core" % "1.0.9",
  "ch.qos.logback" % "logback-classic" % "1.0.9",
  //////////////////////////////////////////////////////////////////////
  // Clients
  "redis.clients" % "jedis" % "2.1.0",
  "org.hectorclient" % "hector-core" % "1.1-1" excludeAll(
    ExclusionRule(organization = "log4j"),
    ExclusionRule(organization = "org.slf4j"),
    ExclusionRule(organization = "org.apache.thrift")
  ),
  "com.googlecode.xmemcached" % "xmemcached" % "1.3.8",
  "mysql" % "mysql-connector-java" % "5.1.15",
  //////////////////////////////////////////////////////////////////////
  // Buzzinate components
  "com.buzzinate" % "buzz-utils" % "1.1-SNAPSHOT" excludeAll(
    ExclusionRule(organization = "org.springframework")
  ),
  "com.buzzinate.buzzads" % "common" % "1.1-SNAPSHOT" excludeAll(
    ExclusionRule(organization = "org.apache.thrift")
  ),
  //////////////////////////////////////////////////////////////////////
  // Test
  "org.scalatest" %% "scalatest" % "1.8" % "test",
  "com.yammer.metrics" % "metrics-core" % "2.2.0",
  "com.yammer.metrics" % "metrics-scala_2.9.1" % "2.0.0",
  "com.yammer.metrics" % "metrics-web" % "2.1.1"
 )

scalaVersion := "2.9.1"