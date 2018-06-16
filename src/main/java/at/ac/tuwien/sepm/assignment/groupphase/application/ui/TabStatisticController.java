package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.Notifiable;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.StatisticService;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.Map;

@Controller
public class TabStatisticController implements Notifiable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private StackedBarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis recipesAxis;

    @FXML
    private NumberAxis quantityAxis;

    private StatisticService statisticService;
    private NotificationService notificationService;

    public TabStatisticController(StatisticService statisticService, NotificationService notificationService) {
        this.statisticService = statisticService;
        this.notificationService = notificationService;
    }

    @FXML
    public void initialize() {
        notificationService.subscribeTo(TabStatisticController.class, this);
        barChart.setLegendVisible(false);
        quantityAxis.setAutoRanging(false);
        quantityAxis.setTickUnit(1);
        quantityAxis.setMinorTickVisible(false);
        recipesAxis.setTickLabelFont(Font.font(15));
        updateBarChart();
    }

    @Override
    public void onNotify() {
        updateBarChart();
    }

    private void updateBarChart() {
        LOG.debug("Updating line chart data.");
        barChart.getData().clear();

        Map<Recipe, Integer> mostPopularRecipes;
        try {
            mostPopularRecipes = statisticService.getMostPopularRecipes();

            var iterator = mostPopularRecipes.entrySet().iterator();
            if (iterator.hasNext())
                quantityAxis.setUpperBound(iterator.next().getValue() + 1);
            else
                quantityAxis.setUpperBound(10);

            for (Map.Entry<Recipe, Integer> entry : mostPopularRecipes.entrySet()) {
                Recipe r = entry.getKey();
                Integer quantity = entry.getValue();

                StackedBarChart.Series<String, Integer> series = new StackedBarChart.Series<>();
                StackedBarChart.Data<String, Integer> data = new StackedBarChart.Data<>(wrapLabelText(r.getName()), quantity);

                series.getData().add(data);
                barChart.getData().add(series);

                Tooltip tooltip = new Tooltip();
                tooltip.setText((int) Math.ceil(r.getCalories()) + " kcal\n" +
                    (int) Math.ceil(r.getCarbohydrates()) + "g Carbohydrates\n" +
                    (int) Math.ceil(r.getProteins()) + "g Proteins\n" +
                    (int) Math.ceil(r.getFats()) + "g Fats");
                Tooltip.install(data.getNode(), tooltip);
            }
        } catch (ServiceInvokationException e) {
            UserInterfaceUtility.handleFaults(e.getContext());
        } catch (Exception e) {
            UserInterfaceUtility.handleFault(e);
        }
    }

    private String wrapLabelText(String str) {
        StringBuilder wrappedLine = new StringBuilder(str.length() + 16);
        int wrapLength = 25, offset = 0;

        while ((str.length() - offset) > wrapLength) {
            if (str.charAt(offset) == ' ')
                offset++;
            else {
                int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

                if (spaceToWrapAt >= offset) {
                    wrappedLine.append(str, offset, spaceToWrapAt);
                    offset = spaceToWrapAt + 1;
                } else {
                    wrappedLine.append(str, offset, wrapLength + offset);
                    offset += wrapLength;
                }
                wrappedLine.append(System.lineSeparator());
            }
        }
        wrappedLine.append(str, offset, str.length());
        return wrappedLine.toString();
    }
}
