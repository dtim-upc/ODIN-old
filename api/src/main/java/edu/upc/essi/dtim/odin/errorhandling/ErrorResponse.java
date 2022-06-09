package edu.upc.essi.dtim.odin.errorhandling;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorResponse {

    private String type;
    private String code;
    private String details;
    private String location;
    private String moreInfo;

    @Override
    public String toString() {

        StringBuilder errorResponsesb = new StringBuilder();
        errorResponsesb.append("ErrorResponse [ ");

        if (type != null) {
            errorResponsesb.append("type=").append(type).append(" ");
        }

        if (code != null) {
            errorResponsesb.append("code=").append(code).append(" ");
        }

        if (details != null) {
            errorResponsesb.append("details=").append(details).append(" ");
        }

        if (location != null) {
            errorResponsesb.append("location=").append(location).append(" ");
        }

        if (moreInfo != null) {
            errorResponsesb.append("moreInfo=").append(moreInfo);
        }

        errorResponsesb.append(" ]");

        return errorResponsesb.toString();
    }

}
