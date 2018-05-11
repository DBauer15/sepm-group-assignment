package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class MainController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private TabPlansController tabPlansController;
    @FXML
    private TabRecipesController tabRecipesController;
    @FXML
    private TabSearchController tabSearchController;

    public MainController(){

    }
}
