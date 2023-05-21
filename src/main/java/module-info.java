module sacd {
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires java.desktop;
	
	opens sopore.net.sacd to javafx.fxml;
	
	exports sopore.net.sacd;
}