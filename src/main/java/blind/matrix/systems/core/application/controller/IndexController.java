package blind.matrix.systems.core.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/datatable-for-huge-data")
    public String dataTableForHugeData() {
        return "datatable-for-huge-data";
    }

}