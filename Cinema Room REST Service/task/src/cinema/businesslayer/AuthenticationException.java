package cinema.businesslayer;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String cause) {
        super(cause);
    }
}
