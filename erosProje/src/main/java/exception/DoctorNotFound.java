package exception;

public class DoctorNotFound extends RuntimeException {
    public DoctorNotFound(String message) {
        super(message);
    }
}
