package cinema.businesslayer;

public class RequestProcessingException extends RuntimeException {
    public RequestProcessingException(String cause) {
        super(cause);
    }
}
