package blind.matrix.systems.core.application.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequestMapping("/rest/blind/matrix/systems")
public class BlindMatrixSystemsRestController {

    @PostMapping("/")
    public String index() {
        return "upload";
    }

}
