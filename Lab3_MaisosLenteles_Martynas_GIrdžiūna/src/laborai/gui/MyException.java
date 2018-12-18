package laborai.gui;

/**
 * Nuosava situacija, panaudota dialogo struktūrose įvedamų parametrų
 * tikrinimui.
 */
public class MyException extends RuntimeException {

    // Situacijos reikšmė
    private String value;

    public MyException(String message) {
        this(message, "");
    }

    public MyException(String message, String value) {
        super(message);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
