package blank.matrix.systems.core.microservice.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class UniversalErrorExceptionMessage {

    private String errorCode;
    private String errorDesc;
    private String errorStackTrace;
    private String errorRootCauseMessage;

    public UniversalErrorExceptionMessage(String errorCode, String errorRootCauseMessage, String errorStackTrace, String errorDesc) {
        this.errorCode = errorCode;
        this.errorRootCauseMessage = errorRootCauseMessage;
        this.errorStackTrace = errorStackTrace;
        this.errorDesc = errorDesc;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    public void setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
    }

    public String getErrorRootCauseMessage() {
        return errorRootCauseMessage;
    }

    public void setErrorRootCauseMessage(String errorRootCauseMessage) {
        this.errorRootCauseMessage = errorRootCauseMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("errorCode", errorCode)
                .append("errorDesc", errorDesc)
                .append("errorStackTrace", errorStackTrace)
                .append("errorRootCauseMessage", errorRootCauseMessage)
                .toString();
    }
}
