package exception;

public class TheBestMatchingDoctorNotFound extends RuntimeException {
    public TheBestMatchingDoctorNotFound(String message) {
        super(message);
    }
}
