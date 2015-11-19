package freelance.android.erick.demoapplication.model;

/**
 * Created by erick on 19.11.15.
 */
public class Response {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String status;
    public String code;
    public String message;
}
