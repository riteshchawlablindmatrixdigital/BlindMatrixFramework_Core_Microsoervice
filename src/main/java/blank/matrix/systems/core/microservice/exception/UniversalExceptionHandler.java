package blank.matrix.systems.core.microservice.exception;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public class UniversalExceptionHandler extends ResponseEntityExceptionHandler {


    public static HttpStatus getStatus(jakarta.servlet.http.HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    public static String getControllerException(jakarta.servlet.http.HttpServletRequest request, Throwable ex) {
        return new UniversalErrorExceptionMessage(getStatus(request).getReasonPhrase(),
                StringUtils.join("\n Root Cause Message: ", ExceptionUtils.getRootCauseMessage(ex)),
                StringUtils.join("\n Error Message: ", ExceptionUtils.getMessage(ex)),
                StringUtils.join("\n Error Message: ", ex.getMessage())).toString();
    }

    public static HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
