import scala.language.postfixOps
import scala.sys.process.Process

name := """news-ui"""
organization := "com.b2c.gated"

version := "1.3.6"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"
autoScalaLibrary := false

libraryDependencies ++= Seq(
  ws,
  guice
)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers ++= Seq(
"Typesafe Releases Repository" at "https://repo.typesafe.com/typesafe/releases/",
"Typesafe Snapshots Repository" at "https://repo.typesafe.com/typesafe/snapshots/",
"Jastice Snapshot" at "https://dl.bintray.com/jastice/maven",
"google-sedis-fix" at "https://pk11-scratch.googlecode.com/svn/trunk"
)

fork in run :=true

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.b2c.shopx.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.b2c.shopx.binders._"

//UI Build Script Starts

val Success = 0
val Error = 1

PlayKeys.playRunHooks += baseDirectory.map(UIBuild.apply).value

val isWindows = System.getProperty("os.name").toLowerCase().contains("win")

def runScript(script: String)(implicit dir: File): Int = {
  if(isWindows){ Process("cmd /c " + script, dir) } else { Process(script, dir) } }!

def uiWasInstalled(implicit dir: File): Boolean = (dir / "node_modules").exists()

def runNpmInstall(implicit dir: File): Int =
  if (uiWasInstalled) Success else runScript("npm install")

def ifUiInstalled(task: => Int)(implicit dir: File): Int =
  if (runNpmInstall == Success) task
  else Error

def runProdBuild(implicit dir: File): Int = ifUiInstalled(runScript("npm run build-prod"))

def runDevBuild(implicit dir: File): Int = ifUiInstalled(runScript("npm run build"))

def runUiTests(implicit dir: File): Int = ifUiInstalled(runScript("npm test-no-watch"))

lazy val `ui-dev-build` = taskKey[Unit]("run ui tests when develping the application.")

`ui-dev-build` := {
  implicit val UIroot = baseDirectory.value / "ui"
  if (runDevBuild != Success) throw new Exception("oops! UI Build crashed")
}

lazy val `ui-prod-build` = taskKey[Unit]("run ui build when packaging the application.")

`ui-prod-build` := {
  implicit val UIroot = baseDirectory.value / "ui"
  if (runProdBuild != Success) throw new Exception("ops! ui build crashed.")
}

lazy val `ui-test` = taskKey[Unit]("run UI tests when testing application.")

`ui-test` := {
  implicit val UIroot = baseDirectory.value / "ui"
  if (runUiTests != 0) throw new Exception("ops! ui test failed.")
}

lazy val hello = taskKey[Unit]("prints 'Hello World'.")

hello := {
  implicit val UIroot = baseDirectory.value / "ui"
  if (runProdBuild != Success) throw new Exception("ops! ui build crashed.")
}

`ui-test`:= (`ui-test` dependsOn `ui-dev-build`).value

dist := (dist dependsOn `ui-prod-build`).value
stage := (stage dependsOn `ui-prod-build`).value
test := ((test in Test) dependsOn `ui-test`).value

//UI Build Script Ends


