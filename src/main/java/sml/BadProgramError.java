package sml;

public class BadProgramError extends Exception {

    private final String message;
    public BadProgramError(String message) {
        super();
        this.message = message;
    }

    public void printMessage() {
        System.out.println(message);
    }

    public String getMessage() {
        return message;
    }
}
