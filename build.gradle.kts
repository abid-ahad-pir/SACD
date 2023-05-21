plugins {
  id("application")
  id("eclipse")
  id("org.openjfx.javafxplugin") version "0.0.13"
  id("org.beryx.jlink") version "2.26.0"
}

repositories {
    mavenCentral()
}

application {
    mainModule.set("$moduleName")
    mainClass.set("sopore.net.sacd.MainApp")
}

javafx {
    version = "17"
    modules = listOf( "javafx.controls", "javafx.fxml" )
}

dependencies{
	implementation("io.github.leovr:rtp-midi-javax-sound-midi:2.0.1")
}