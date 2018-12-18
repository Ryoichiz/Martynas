package laborai.gui.swing;

import java.awt.Color;
import javax.swing.JTextArea;

/**
 * Klasė, skirta duomenų išvedimui į Swing GUI
 */
public class KsSwing {

    private static int lineNr;
    private static boolean formatStartOfLine = true;

    private static String getStartOfLine() {
        return (formatStartOfLine) ? ++lineNr + "| " : "";
    }

    public static void setFormatStartOfLine(boolean formatStartOfLine) {
        KsSwing.formatStartOfLine = formatStartOfLine;
    }

    public static void ou(JTextArea ta, Object o) {
        StringBuilder sb = new StringBuilder();
        if (o instanceof Iterable) {
            ((Iterable) o).forEach(p -> sb.append(p).append(System.lineSeparator()));
        } else {
            sb.append(o.toString());
        }
        ta.append(sb.toString());
    }

    public static void oun(JTextArea ta, Object o) {
        StringBuilder sb = new StringBuilder();
        if (o instanceof Iterable) {
            ((Iterable) o).forEach(p -> sb.append(p).append(System.lineSeparator()));
            sb.append(System.lineSeparator());
        } else {
            sb.append(o.toString()).append(System.lineSeparator());
        }
        ta.append(sb.toString());
    }

    public static void ou(JTextArea ta, Object o, String msg) {
        String startOfLine = getStartOfLine();
        ta.append(startOfLine + msg + ": ".intern());
        oun(ta, o);
    }

    public static void oun(JTextArea ta, Object o, String msg) {
        String startOfLine = getStartOfLine();
        ta.append(startOfLine + msg + ": ".intern() + System.lineSeparator());
        oun(ta, o);
    }

    public static void ounArgs(JTextArea ta, String format, Object... args) {
        String startOfLine = getStartOfLine();
        ta.append(startOfLine + String.format(format, args) + System.lineSeparator());
    }

    public static void ounerr(JTextArea ta, Exception e) {
        ta.setBackground(Color.pink);
        String startOfLine = getStartOfLine();
        ta.append(new StringBuilder()
                .append(startOfLine)
                .append(e.getLocalizedMessage())
                .append(System.lineSeparator())
                .toString());
    }

    public static void ounerr(JTextArea ta, String msg) {
        ta.setBackground(Color.pink);
        String startOfLine = getStartOfLine();
        ta.append(startOfLine + msg + System.lineSeparator());
    }

    public static void ounerr(JTextArea ta, String msg, String parameter) {
        ta.setBackground(Color.pink);
        String startOfLine = getStartOfLine();
        ta.append(startOfLine + msg + ((parameter == null || parameter.isEmpty())
                ? "" : ": ".intern() + parameter) + System.lineSeparator());
    }
}
