package blind.matrix.systems.core.application.controller;

import blind.matrix.systems.core.application.dtos.RequestData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @GetMapping("/basic-data")
    public ModelAndView showForm() {
        RequestData requestData = new RequestData();
        return new ModelAndView("basic-data", "requestData", requestData);
    }

    @PostMapping("/basic-data-details")
    public String indexNext(@ModelAttribute("requestData") RequestData requestData,
                                  BindingResult result, ModelMap model) {
        model.addAttribute("query", requestData.getQuery());
        model.addAttribute("numberOfResults", requestData.getNumberOfResults());
        model.addAttribute("a", Math.random());
        return "basic-data";
    }

    @GetMapping("/huge-data")
    public ModelAndView dataTableForHugeData() {
        return new ModelAndView("huge-data", "requestData", new RequestData());
    }

    @PostMapping("/huge-data-details")
    public String hugeDataSolution(@ModelAttribute("requestData") RequestData requestData,
                            BindingResult result, ModelMap model) {
        model.addAttribute("query", requestData.getQuery());
        model.addAttribute("numberOfResults", requestData.getNumberOfResults());
        return "huge-data";
    }
}