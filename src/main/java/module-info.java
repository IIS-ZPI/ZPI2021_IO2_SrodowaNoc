module com.srodowanoc.zpi {
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.media;
	requires javafx.base;
	requires javafx.web;
	requires javafx.swing;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires validatorfx;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires eu.hansolo.tilesfx;

	opens com.srodowanoc.zpi to javafx.fxml;
	exports com.srodowanoc.zpi;
}