package at.ac.tuwien.sepm.assignment.groupphase.main;

import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;

@Component
@ComponentScan("at.ac.tuwien.sepm.assignment.groupphase")
public final class MainApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AnnotationConfigApplicationContext context;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // setup main
        primaryStage.setTitle("FoodOrca");
        primaryStage.setWidth(1068);
        primaryStage.setHeight(884);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> LOG.debug("Application shutdown initiated"));

        context = new AnnotationConfigApplicationContext(MainApplication.class);
        /*final var fxmlLoader = context.getBean(SpringFXMLLoader.class);

          primaryStage.setScene(new Scene((Parent) fxmlLoader.load(
            getClass().getResourceAsStream("/fxml/Main.fxml")))); */

            try (InputStream fxmlStream = getClass().getResourceAsStream("/fxml/Main.fxml")) {
                FXMLLoader loader = new FXMLLoader();
                // set location of fxml files to FXMLLoader
                URL location = getClass().getResource("/fxml/Main.fxml");
                loader.setLocation(location);
                // set controller factory
                loader.setControllerFactory(context::getBean);
                // load FXML
                primaryStage.setScene(new Scene(loader.load(fxmlStream)));
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }

        // show application
        primaryStage.show();
        primaryStage.toFront();
        LOG.debug("Application startup complete");
    }

    public static void main(String[] args) {
        LOG.debug("Application starting with arguments={}", (Object) args);
        // TODO Enable this
        Application.launch(MainApplication.class, args);
    }

    @Override
    public void stop() {
        LOG.debug("Stopping main");
        context.close();
    }
}
