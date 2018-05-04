package at.ac.tuwien.sepm.assignment.groupphase.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
@ComponentScan("at.ac.tuwien.sepm.assignment.groupphase")
public final class MainApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AnnotationConfigApplicationContext context;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // setup main
        // TODO Start here
        LOG.debug("Application startup complete");
    }

    public static void main(String[] args) {
        LOG.debug("Application starting with arguments={}", (Object) args);
        // TODO Enable this
        //Application.launch(MainApplication.class, args);
    }

    @Override
    public void stop() {
        LOG.debug("Stopping main");
        context.close();
    }
}
