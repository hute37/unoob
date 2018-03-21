//import AssemblyKeys._
import sbt.Keys._
import sbt._
import ReleaseTransformations._
//import Utility._


name := "unoob"

version := "0.1"

/*
Disable test parallel execution due to shared spark context
 */
//concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)

scalaVersion := "2.10.7"

val commonSettings = Seq(

  scalaVersion := "2.10.7",

  scalacOptions := Seq(
    "-deprecation",
    "-feature",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
    "-language:postfixOps"
  ),
  fork := true,
  resolvers := Seq(
    "maven"    at "https://repo.maven.apache.org/maven2/",
    "java.net" at "http://download.java.net/maven/2/",
    "cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos",
    "typesafe" at "http://repo.typesafe.com/typesafe/ivy-releases/",
    Resolver.mavenLocal,
    Resolver.typesafeRepo("releases"),
    Resolver.sonatypeRepo("releases")
  ),
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,              // : ReleaseStep
    inquireVersions,                        // : ReleaseStep
//    runClean,                               // : ReleaseStep
//    runTest,                                // : ReleaseStep
    setReleaseVersion,                      // : ReleaseStep
//    commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
//    tagRelease                             // : ReleaseStep
    publishArtifacts                       // : ReleaseStep, checks whether `publishTo` is properly set up
//    setNextVersion                         // : ReleaseStep
//    commitNextVersion,                      // : ReleaseStep
//    pushChanges                             // : ReleaseStep, also checks that an upstream branch is properly configured
  ),
  releaseUseGlobalVersion := false
)


val sparkVersion = "1.6.0"


// resolvers ++= Seq(
//   "apache-snapshots" at "http://repository.apache.org/snapshots/"
// )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-hive" % sparkVersion % "provided"
)

//libraryDependencies += "org.ddahl" %% "rscala" % "2.5.0"


lazy val root = project.in(file("."))
  .settings(commonSettings:_*)
  // .settings(libraryDependencies := libraryDependencies)
//.dependsOn(common, tStats, tStatsTime, tPrefs, clv, rfm)
  .settings(mainClass in assembly := Some("org.here.unoob.app.Main"))
//.settings(jarName in assembly := chooseNameByEnv)
  .settings(javacOptions ++= Seq("-source", "1.7"))
  .settings(javacOptions ++= Seq("-target", "1.7"))
  .settings(javacOptions ++= Seq("-verbose"))

val all = Project("all", file("dontuseme")) aggregate(root)

