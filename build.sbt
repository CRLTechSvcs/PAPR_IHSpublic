name := """issue"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  filters,
  cache
)     

libraryDependencies ++= Seq(
  "commons-lang" % "commons-lang" % "2.4", 
  "commons-io" % "commons-io" % "2.4",  
  "mysql" % "mysql-connector-java" % "5.1.21",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4" ,
  "be.objectify"  %% "deadbolt-java"     % "2.3.0-RC1",
  "com.itextpdf" % "itextpdf" % "5.0.6" ,
  "org.apache.poi" % "poi" % "3.10-FINAL",
  "org.apache.poi" % "poi-ooxml" % "3.10-FINAL",
  "org.quartz-scheduler" % "quartz" % "2.2.1"
)

resolvers ++= Seq(
  Resolver.url("Objectify Play Repository", url("http://schaloner.github.io/releases/"))(Resolver.ivyStylePatterns)
)