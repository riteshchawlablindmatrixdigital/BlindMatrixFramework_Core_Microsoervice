package blank.matrix.systems.core.microservice.controller;

import blank.matrix.systems.core.microservice.dtos.BlankMatrixSystemsRequestData;
import blank.matrix.systems.core.microservice.exception.UniversalExceptionHandler;
import blank.matrix.systems.core.microservice.services.BlankMatrixSystemsServices;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlankMatrixSystemsController {

    @Autowired
    private BlankMatrixSystemsServices blankMatrixSystemsServices;

    @GetMapping("/basic-data")
    public ModelAndView showForm() {
        BlankMatrixSystemsRequestData blankMatrixSystemsRequestData = new BlankMatrixSystemsRequestData();

        return new ModelAndView("basic-data", "blankMatrixSystemsRequestData", blankMatrixSystemsRequestData);
    }

    @PostMapping("/basic-data-details")
    public ModelAndView basicDataDetails(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @ModelAttribute("blankMatrixSystemsRequestData") BlankMatrixSystemsRequestData blankMatrixSystemsRequestData,
                                   BindingResult result, ModelMap model) {
        String resultDataString = "";
        try {
            model.addAttribute("query", blankMatrixSystemsRequestData.getQuery());
            model.addAttribute("numberOfResults", blankMatrixSystemsRequestData.getNumberOfResults());
            model.addAttribute("a", Math.random());
            resultDataString = blankMatrixSystemsServices.executeQueryResults(request, response, blankMatrixSystemsRequestData);
            if(StringUtils.isNotEmpty(resultDataString) && resultDataString.equalsIgnoreCase("No Data Available for the Query")) {
                model.addAttribute("NoResultsFound", "No Data Available for the Query. Kindly modify the query and Try again.");
            }
            model.addAttribute("resultData", resultDataString);
        } catch (Exception e) {
            String errorString = UniversalExceptionHandler.getControllerException(request, e).toString();
            model.addAttribute("error", errorString);
        }
        return new ModelAndView("basic-data", "blankMatrixSystemsRequestData", blankMatrixSystemsRequestData);
    }

    @GetMapping("/huge-data")
    public ModelAndView dataTableForHugeData() {
        return new ModelAndView("huge-data", "blankMatrixSystemsRequestData", new BlankMatrixSystemsRequestData());
    }

    @PostMapping("/huge-data-details")
    public String hugeDataSolution(@ModelAttribute("blankMatrixSystemsRequestData") BlankMatrixSystemsRequestData blankMatrixSystemsRequestData,
                            BindingResult result, ModelMap model) {
        model.addAttribute("query", blankMatrixSystemsRequestData.getQuery());
        model.addAttribute("numberOfResults", blankMatrixSystemsRequestData.getNumberOfResults());
        return "huge-data";
    }
}