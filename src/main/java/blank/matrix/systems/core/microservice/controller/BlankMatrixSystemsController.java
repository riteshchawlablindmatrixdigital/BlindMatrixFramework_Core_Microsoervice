package blank.matrix.systems.core.microservice.controller;

import blank.matrix.systems.core.microservice.dtos.BlankMatrixSystemsRequestData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlankMatrixSystemsController {

    @GetMapping("/basic-data")
    public ModelAndView showForm() {
        BlankMatrixSystemsRequestData blankMatrixSystemsRequestData = new BlankMatrixSystemsRequestData();
        return new ModelAndView("basic-data", "requestData", blankMatrixSystemsRequestData);
    }

    @PostMapping("/basic-data-details")
    public String indexNext(@ModelAttribute("requestData") BlankMatrixSystemsRequestData blankMatrixSystemsRequestData,
                                  BindingResult result, ModelMap model) {
        model.addAttribute("query", blankMatrixSystemsRequestData.getQuery());
        model.addAttribute("numberOfResults", blankMatrixSystemsRequestData.getNumberOfResults());
        model.addAttribute("a", Math.random());
        return "basic-data";
    }

    @GetMapping("/huge-data")
    public ModelAndView dataTableForHugeData() {
        return new ModelAndView("huge-data", "requestData", new BlankMatrixSystemsRequestData());
    }

    @PostMapping("/huge-data-details")
    public String hugeDataSolution(@ModelAttribute("requestData") BlankMatrixSystemsRequestData blankMatrixSystemsRequestData,
                            BindingResult result, ModelMap model) {
        model.addAttribute("query", blankMatrixSystemsRequestData.getQuery());
        model.addAttribute("numberOfResults", blankMatrixSystemsRequestData.getNumberOfResults());
        return "huge-data";
    }
}