package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.Notifiable;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.StatisticService;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TabStatisticController implements Notifiable {

    @FXML
    private BarChart<String, Integer> lineChart;

    @FXML
    private CategoryAxis recipesAxis;

    @FXML
    private NumberAxis quantityAxis;

    private StatisticService statisticService;

    @FXML
    public void initialize(){
    }

    @Override
    public void onNotify() {
        updateLineChart();
    }

    public TabStatisticController(StatisticService statisticService){
        this.statisticService = statisticService;
    }

    private void updateLineChart(){
        lineChart.getData().clear();

        Map<Recipe, Integer> mostPopularRecipes = new HashMap<>();
        try {
            mostPopularRecipes = statisticService.getMostPopularRecipes();
        } catch (ServiceInvokationException e) {
            UserInterfaceUtility.handleFaults(e.getContext());
        } catch (Exception e) {
            UserInterfaceUtility.handleFault(e);
        }
    }
}
