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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.Map;

@Controller
public class TabStatisticController implements Notifiable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis recipesAxis;

    @FXML
    private NumberAxis quantityAxis;

    private StatisticService statisticService;

    @FXML
    public void initialize(){
        barChart.setLegendVisible(false);
        quantityAxis.setTickUnit(1);
        quantityAxis.setMinorTickVisible(false);
        updateLineChart();
    }

    @Override
    public void onNotify() {
        updateLineChart();
    }

    public TabStatisticController(StatisticService statisticService){
        this.statisticService = statisticService;
    }

    private void updateLineChart(){
        LOG.debug("Updating line chart data.");
        barChart.getData().clear();

        Map<Recipe, Integer> mostPopularRecipes;
        try {
            mostPopularRecipes = statisticService.getMostPopularRecipes();

            for(Map.Entry<Recipe, Integer> entry : mostPopularRecipes.entrySet()){
                Recipe r = entry.getKey();
                Integer quantity = entry.getValue();
                BarChart.Series<String, Integer> series = new BarChart.Series<>();

                series.getData().add(new BarChart.Data<>(r.getName(), quantity));
                barChart.getData().add(series);
            }
        } catch (ServiceInvokationException e) {
            UserInterfaceUtility.handleFaults(e.getContext());
        } catch (Exception e) {
            UserInterfaceUtility.handleFault(e);
        }

    }
}
