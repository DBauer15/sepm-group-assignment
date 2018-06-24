package at.ac.tuwien.sepm.assignment.groupphase.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import at.ac.tuwien.sepm.assignment.groupphase.application.ui.MainController;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;

@Component
@ComponentScan("at.ac.tuwien.sepm.assignment.groupphase")
public final class MainApplication extends Application {

	private static final String MAIN_FXML = "/fxml/Main.fxml";
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private AnnotationConfigApplicationContext context;

	@Override
	public void start(Stage primaryStage) throws Exception {
	    // load icon
	    loadIcon();

		// setup main
		primaryStage.setTitle("FoodOrca");
		primaryStage.centerOnScreen();
		primaryStage.setOnCloseRequest(event -> LOG.debug("Application shutdown initiated"));
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("/img/foodOrca.png"));

		// load files
		context = new AnnotationConfigApplicationContext(MainApplication.class);
		final var fxmlLoader = context.getBean(SpringFXMLLoader.class);
		fxmlLoader.setLocation(getClass().getResource(MAIN_FXML));

		var load = fxmlLoader.loadAndWrap(getClass().getResourceAsStream(MAIN_FXML), MainController.class);
		primaryStage.setScene(new Scene((Parent) load.getLoadedObject()));
		load.getController().initializeView();

		// show application
		primaryStage.show();
		primaryStage.toFront();
		LOG.debug("Application startup complete");
	}

	public static void main(String[] args) {
		LOG.debug("Application starting with arguments={}", (Object) args);
		Application.launch(MainApplication.class, args);
	}

	@Override
	public void stop() {
		LOG.debug("Stopping main");
		try {
			JDBCConnectionManager.getConnection().close();
		} catch (SQLException e) {
			LOG.info("JDBC Connection could not be closed on application exit.");
		}
		context.close();
	}

	private static void loadIcon(){

	    if(!(System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0)){
	        return;
        }

        try {
            File pathToFile = new File("./src/main/resources/img/foodOrca.png");
            BufferedImage image = ImageIO.read(pathToFile);
            Taskbar.getTaskbar().setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
