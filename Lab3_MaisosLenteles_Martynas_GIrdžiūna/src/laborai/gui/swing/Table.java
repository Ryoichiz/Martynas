package laborai.gui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Klasėje specifikuota apibrėžtas savybes turinti lentelė. Naudojama maišos
 * lentelės atvaizdavimui.
 *
 * @author darius
 */
public class Table extends JTable {

    public void setModel(TableModel model, int colWidth) {
        if (model == null) {
            throw new IllegalArgumentException("TableModel is null");
        }
        if (colWidth < 0) {
            throw new IllegalArgumentException("Table column width is <0: " + colWidth);
        }
        setModel(model);
        appearance(colWidth);
    }

    private void appearance(int colWidth) {
        setShowGrid(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Celės stilius - pacentruojame
        DefaultTableCellRenderer toCenter = new DefaultTableCellRenderer();
        toCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < getColumnCount(); i++) {
            if (i == 0) {
                getColumnModel().getColumn(i).setPreferredWidth(55);
                // Nustatome nulinio stulpelio celių stilių
                getColumnModel().getColumn(i).setCellRenderer(toCenter);
            } else if (i % 2 != 0) {
                getColumnModel().getColumn(i).setPreferredWidth(20);
                // Nustatome stulpelių su rodyklėmis celių stilių
                getColumnModel().getColumn(i).setCellRenderer(toCenter);
            } else {
                getColumnModel().getColumn(i).setMaxWidth(colWidth);
                getColumnModel().getColumn(i).setMinWidth(colWidth);
            }
        }

        // Lentelės antraštės
        getTableHeader().setResizingAllowed(false);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        // Išcentruojamos antraštės
        ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        // Nustatomas tooltips'ų rodymas
        String value = (String) getValueAt(row, column);
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            jc.setToolTipText(value);
        }
        // Morkine spalva nuspalvinamos celės, kuriose kas nors įrašyta, išskyrus rodyklę
        if (value != null && !value.equals("") && !value.equals("-->")) {
            c.setBackground(Color.ORANGE);//new Color(204, 255, 204));
        } //Baltai - likusias celes
        else {
            c.setBackground(Color.WHITE);
        }
        return c;
    }
}
