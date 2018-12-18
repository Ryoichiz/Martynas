package laborai.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import laborai.gui.MyException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import laborai.demo.AutoGamyba;
import laborai.demo.Automobilis;
import laborai.demo.GreitaveikosTyrimas;
import laborai.studijosktu.HashType;
import laborai.studijosktu.Ks;
import laborai.studijosktu.MapADTx;
import laborai.studijosktu.MapKTUx;

/**
 * Lab3 langas su Swing'u
 * <pre>
 *                  BorderLayout
 *              (Center)               (East)
 * |----------scrollTable----------|-scrollEast-|
 * |                               |            |
 * |                               |            |
 * |                               |            |
 * |                               |            |
 * |             table             |            |
 * |                               |            |
 * |                               |            |
 * |                               |------------|
 * |                               | panButtons |
 * |                               |            |
 * |----------------panParam123Events-----------|
 * |-------scrollParam123--------|-scrollEvents-|
 * |---------|---------|---------|              |
 * |panParam1|panParam2|panParam3|   taEvents   |
 * |---------|---------|---------|--------------|
 *                   (South)
 * </pre>
 *
 * @author darius.matulis@ktu.lt
 */
public class Lab3Window extends JFrame implements ActionListener {

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("laborai.gui.messages");

    private static final int TF_WIDTH = 6;
    private static final int NUMBER_OF_BUTTONS = 4;

    private final JComboBox cmbCollisionTypes = new JComboBox();
    private final JComboBox cmbHashFunctions = new JComboBox();
    private final JTextArea taInput = new JTextArea();
    private final Table table = new Table();
    private final JScrollPane scrollTable = new JScrollPane(table);
    private final JPanel panParam123 = new JPanel();
    private final JScrollPane scrollParam123 = new JScrollPane(panParam123);
    private final JPanel panParam123Events = new JPanel();
    private final JTextArea taEvents = new JTextArea();
    private final JScrollPane scrollEvents = new JScrollPane(taEvents);
    private final JPanel panEast = new JPanel();
    private final JScrollPane scrollEast = new JScrollPane(panEast);
    private Panels panParam1, panParam2, panParam3, panButtons;
    private Menu menus;

    private HashType ht = HashType.DIVISION;
    private MapADTx<String, Automobilis> map;
    private int sizeOfInitialSubSet, sizeOfGenSet, colWidth, initialCapacity;
    private float loadFactor;
    private final AutoGamyba autoGamyba = new AutoGamyba();

    public Lab3Window() {
        initComponents();
    }

    private void initComponents() {
        // Formuojamas rožinės spalvos panelis (dešinėje pusėje)   
        // Užpildomi JComboBox'ai
        Stream.of(MESSAGES.getString("cmbCollisionType1"),
                MESSAGES.getString("cmbCollisionType2"),
                MESSAGES.getString("cmbCollisionType3"),
                MESSAGES.getString("cmbCollisionType4"))
                .forEach(s -> cmbCollisionTypes.addItem(s));
        cmbCollisionTypes.addActionListener(this);

        Stream.of(MESSAGES.getString("cmbHashFunction1"),
                MESSAGES.getString("cmbHashFunction2"),
                MESSAGES.getString("cmbHashFunction3"),
                MESSAGES.getString("cmbHashFunction4"))
                .forEach(s -> cmbHashFunctions.addItem(s));
        cmbHashFunctions.addActionListener(this);

        // Formuojamas mygtukų tinklelis (mėlynas). Naudojama klasė Panels.
        panButtons = new Panels(new String[]{
            MESSAGES.getString("button1"),
            MESSAGES.getString("button2"),
            MESSAGES.getString("button3"),
            MESSAGES.getString("button4")}, 1, NUMBER_OF_BUTTONS);
        panButtons.getButtons().forEach((btn) -> btn.addActionListener(this));
        IntStream.of(1, 3).forEach(p -> panButtons.getButtons().get(p).setEnabled(false));

        // Viskas sudedama į vieną (rožinės spalvos) panelį
        panEast.setLayout(new BoxLayout(panEast, BoxLayout.Y_AXIS));
        Stream.of(new JLabel(MESSAGES.getString("border1")),
                cmbCollisionTypes,
                new JLabel(MESSAGES.getString("border2")),
                cmbHashFunctions,
                new JLabel(MESSAGES.getString("border3")),
                taInput,
                panButtons)
                .forEach(comp -> {
                    comp.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                    panEast.add(Box.createRigidArea(new Dimension(0, 2)));
                    panEast.add(comp);
                });

        // Formuojama pirmoji parametrų lentelė (šviesiai žalia). Naudojama klasė Panels. 
        panParam1 = new Panels(
                new String[]{
                    MESSAGES.getString("lblParam11"),
                    MESSAGES.getString("lblParam12"),
                    MESSAGES.getString("lblParam13"),
                    MESSAGES.getString("lblParam14"),
                    MESSAGES.getString("lblParam15"),
                    MESSAGES.getString("lblParam16")},
                new String[]{
                    MESSAGES.getString("tfParam11"),
                    MESSAGES.getString("tfParam12"),
                    MESSAGES.getString("tfParam13"),
                    MESSAGES.getString("tfParam14"),
                    MESSAGES.getString("tfParam15"),
                    MESSAGES.getString("tfParam16")}, TF_WIDTH);

        // .. tikrinami ivedami parametrai dėl teisingumo. Negali būti neigiami.     
        IntStream.of(0, 1, 2, 4).forEach(v -> panParam1.getTfOfTable().get(v).setInputVerifier(new NotNegativeNumberVerifier()));

        // Tikrinamas įvedamas apkrovimo faktorius. Turi būti (0;1] ribose
        panParam1.getTfOfTable().get(3).setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText().trim();
                try {
                    Float loadFactor = Float.valueOf(text);
                    if (loadFactor <= 0.0 || loadFactor > 1.0) {
                        input.setBackground(Color.RED);
                        return false;
                    }
                    input.setBackground(Color.WHITE);
                    return true;
                } catch (NumberFormatException e) {
                    input.setBackground(Color.RED);
                    return false;
                }
            }
        });
        // Formuojama antroji parametrų lentelė (gelsva). Naudojama klasė Panels 
        panParam2 = new Panels(
                new String[]{
                    MESSAGES.getString("lblParam21"),
                    MESSAGES.getString("lblParam22"),
                    MESSAGES.getString("lblParam23"),
                    MESSAGES.getString("lblParam24"),
                    MESSAGES.getString("lblParam25"),
                    MESSAGES.getString("lblParam26")},
                new String[]{
                    MESSAGES.getString("tfParam21"),
                    MESSAGES.getString("tfParam22"),
                    MESSAGES.getString("tfParam23"),
                    MESSAGES.getString("tfParam24"),
                    MESSAGES.getString("tfParam25"),
                    MESSAGES.getString("tfParam26")}, TF_WIDTH);

        // Formuojama trečioji parametrų lentelė (šviesiai žalia). Naudojama klasė Panels        
        panParam3 = new Panels(
                new String[]{
                    MESSAGES.getString("lblParam31"),
                    MESSAGES.getString("lblParam32"),
                    MESSAGES.getString("lblParam33"),
                    MESSAGES.getString("lblParam34"),
                    MESSAGES.getString("lblParam35"),
                    MESSAGES.getString("lblParam36")},
                new String[]{
                    MESSAGES.getString("tfParam31"),
                    MESSAGES.getString("tfParam32"),
                    MESSAGES.getString("tfParam33"),
                    MESSAGES.getString("tfParam34"),
                    MESSAGES.getString("tfParam35"),
                    MESSAGES.getString("tfParam36")}, TF_WIDTH);

        // Visų trijų parametrų lentelių paneliai sudedami į šviesiai pilką panelį
        Stream.of(panParam1, panParam2, panParam3).forEach(p -> panParam123.add(p));

        // Toliau suformuojamas panelis iš šviesiai pilko panelio ir programos
        // įvykių JTextArea
        GroupLayout gl = new GroupLayout(panParam123Events);
        panParam123Events.setLayout(gl);
        gl.setHorizontalGroup(
                gl.createSequentialGroup().
                        addComponent(scrollParam123,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                Short.MAX_VALUE).
                        addComponent(scrollEvents,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                Short.MAX_VALUE));
        gl.setVerticalGroup(
                gl.createSequentialGroup().
                        addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER).
                                addComponent(scrollParam123).
                                addComponent(scrollEvents)));

        // Kad prijungiant tekstą prie JTextArea vaizdas visada nušoktų į apačią
        scrollEvents.getVerticalScrollBar()
                .addAdjustmentListener((AdjustmentEvent e) -> {
                    taEvents.select(taEvents.getCaretPosition() * taEvents.getFont().getSize(), 0);
                });

        // Suformuojamas bendras Lab3 langas su meniu 
        menus = new Menu(this);
        // Meniu juosta patalpinama šiame freime
        setJMenuBar(menus);

        setLayout(new BorderLayout());
        add(scrollEast, BorderLayout.EAST);
        add(scrollTable, BorderLayout.CENTER);
        add(panParam123Events, BorderLayout.SOUTH);
        appearance();
    }

    /**
     * Kosmetika
     */
    private void appearance() {
        // Rėmeliai
        panParam123.setBorder(new TitledBorder(MESSAGES.getString("border4")));
        scrollTable.setBorder(new TitledBorder(MESSAGES.getString("border5")));
        scrollEvents.setBorder(new TitledBorder(MESSAGES.getString("border6")));

        scrollTable.getViewport().setBackground(Color.white);
        panParam1.setBackground(new Color(204, 255, 204));// Šviesiai žalia
        panParam2.setBackground(new Color(255, 255, 153));// Gelsva
        panParam3.setBackground(new Color(204, 255, 204));// Šviesiai žalia
        panEast.setBackground(Color.pink);
        panButtons.setBackground(new Color(112, 162, 255));// Blyškiai mėlyna
        panParam123.setBackground(Color.lightGray);

        // Antra parametrų lentelė (gelsva) bus neredaguojama
        panParam2.getTfOfTable().forEach((comp) -> comp.setEditable(false));
        Stream.of(taInput, taEvents).forEach(comp -> {
            comp.setBackground(Color.white);
            comp.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        });
        taEvents.setEditable(false);
        scrollEvents.setPreferredSize(new Dimension(350, 0));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        KsSwing.setFormatStartOfLine(true);
        try {
            System.gc();
            System.gc();
            System.gc();
            taEvents.setBackground(Color.white);
            Object source = ae.getSource();
            if (source.equals(panButtons.getButtons().get(0))) {
                mapGeneration(null);
            } else if (source.equals(panButtons.getButtons().get(1))) {
                mapAdd();
            } else if (source.equals(panButtons.getButtons().get(2))) {
                mapEfficiency();
            } else if (source.equals(panButtons.getButtons().get(3))) {
                KsSwing.ounerr(taEvents, MESSAGES.getString("notImplemented"));
            } else if (source.equals(cmbCollisionTypes) || source.equals(cmbHashFunctions)) {
                IntStream.of(1, 3).forEach(p -> panButtons.getButtons().get(p).setEnabled(false));
            }
        } catch (MyException e) {
            KsSwing.ounerr(taEvents, MESSAGES.getString(e.getMessage()), e.getValue());
        } catch (UnsupportedOperationException e) {
            KsSwing.ounerr(taEvents, e.getMessage());
        } catch (Exception e) {
            KsSwing.ounerr(taEvents, MESSAGES.getString("systemError"));
            e.printStackTrace(System.out);
        }
    }

    public void mapGeneration(String filePath) {
        // Išjungiami 2 ir 4 mygtukai
        IntStream.of(1, 3).forEach(p -> panButtons.getButtons().get(p).setEnabled(false));
        // Duomenų nuskaitymas iš parametrų lentelės (žalios)
        readMapParameters();
        // Sukuriamas tuščias atvaizdis priklausomai nuo kolizijų tipo
        createMap();
        // Jei failas nenurodytas - generuojami automobiliai ir talpinami atvaizdyje
        if (filePath == null) {
            Automobilis[] autoArray = autoGamyba.gamintiIrParduotiAutomobilius(sizeOfGenSet, sizeOfInitialSubSet);
            for (Automobilis a : autoArray) {
                map.put(
                        autoGamyba.gautiIsBazesAutoId(),//raktas
                        a);
            }
            KsSwing.ounArgs(taEvents, MESSAGES.getString("msg1"), map.size());
        } else { // Jei failas nurodytas skaitoma iš failo
            map.load(filePath);
            if (map.isEmpty()) {
                KsSwing.ounerr(taEvents, MESSAGES.getString("msg6"), filePath);
            } else {
                KsSwing.ou(taEvents, MESSAGES.getString("msg5"), filePath);
            }
        }

        // Atvaizdis rodomas lentelėje        
        table.setModel(new TableModel(map.getModelList(panParam1.getTfOfTable().get(5).getText()),
                panParam1.getTfOfTable().get(5).getText(), map.getMaxChainSize()), colWidth);
        // Atnaujinamai maišos lentelės parametrai (geltona lentelė)
        updateHashtableParameters(false);
        // Įjungiami 2 ir 4 mygtukai
        IntStream.of(1, 3).forEach(p -> panButtons.getButtons().get(p).setEnabled(true));
    }

    public void mapAdd() throws MyException {
        Automobilis a = autoGamyba.parduotiAutomobili();
        map.put(
                autoGamyba.gautiIsBazesAutoId(),// Raktas
                a);
        table.setModel(new TableModel(map.getModelList(panParam1.getTfOfTable().get(5).getText()),
                panParam1.getTfOfTable().get(5).getText(), map.getMaxChainSize()), colWidth);
        updateHashtableParameters(true);
        KsSwing.oun(taEvents, a, MESSAGES.getString("msg2"));
    }

    public void mapEfficiency() {
        KsSwing.oun(taEvents, "", MESSAGES.getString("msg3"));
        boolean[] statesOfButtons = new boolean[panButtons.getButtons().size()];
        for (int i = 0; i < panButtons.getButtons().size(); i++) {
            statesOfButtons[i] = panButtons.getButtons().get(i).isEnabled();
            panButtons.getButtons().get(i).setEnabled(false);
        }
        cmbCollisionTypes.setEnabled(false);
        cmbHashFunctions.setEnabled(false);
        Arrays.stream(menus.getComponents()).forEach(c -> c.setEnabled(false));
        GreitaveikosTyrimas gt = new GreitaveikosTyrimas();

        // Ši gija paima rezultatus iš greitaveikos tyrimo gijos ir išveda 
        // juos i taEvents. Gija baigia darbą kai gaunama FINISH_COMMAND   
        new Thread(() -> {
            try {
                String result;
                while (!(result = gt.getResultsLogger().take())
                        .equals(GreitaveikosTyrimas.FINISH_COMMAND)) {
                    KsSwing.oun(taEvents, result);
                    gt.getSemaphore().release();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            gt.getSemaphore().release();

            for (int i = 0; i < panButtons.getButtons().size(); i++) {
                panButtons.getButtons().get(i).setEnabled(statesOfButtons[i]);
            }
            cmbCollisionTypes.setEnabled(true);
            cmbHashFunctions.setEnabled(true);
            Arrays.stream(menus.getComponents()).forEach(c -> c.setEnabled(true));
        }, "Greitaveikos_rezultatu_gija").start();

        //Šioje gijoje atliekamas greitaveikos tyrimas
        new Thread(() -> gt.pradetiTyrima(), "Greitaveikos_tyrimo_gija").start();
    }

    private void readMapParameters() {
        int i = 0;
        List<JTextField> tfs = panParam1.getTfOfTable();

        String errorMessages[] = {"badSizeOfInitialSubSet", "badSizeOfGenSet",
            "badInitialCapacity", "badLoadFactor", "badColumnWidth"};
        //Patikrinimas dėl parametrų teisingumo
        for (int j = 0; j < tfs.size(); j++) {
            JTextField tf = tfs.get(j);
            if (tf.getInputVerifier() != null && !tf.getInputVerifier().verify(tf)) {
                throw new MyException(errorMessages[i], tf.getText());
            }
        }

        //Kai parametrai teisingi - juos nuskaitome
        sizeOfInitialSubSet = Integer.valueOf(tfs.get(i++).getText());
        sizeOfGenSet = Integer.valueOf(tfs.get(i++).getText());
        initialCapacity = Integer.valueOf(tfs.get(i++).getText());
        loadFactor = Float.valueOf(tfs.get(i++).getText());
        colWidth = Integer.valueOf(tfs.get(i++).getText());

        switch (cmbHashFunctions.getSelectedIndex()) {
            case 0:
                ht = HashType.DIVISION;
                break;
            case 1:
                ht = HashType.MULTIPLICATION;
                break;
            case 2:
                ht = HashType.JCF7;
                break;
            case 3:
                ht = HashType.JCF8;
                break;
            default:
                ht = HashType.DIVISION;
                break;
        }
    }

    private void createMap() {
        switch (cmbCollisionTypes.getSelectedIndex()) {
            case 0:
                map = new MapKTUx<>(new String(), new Automobilis(), initialCapacity, loadFactor, ht);
                break;
            // ...
            // Programuojant kitus kolizijų sprendimo metodus reikia papildyti switch sakinį
            default:
                IntStream.of(1, 3).forEach(p -> panButtons.getButtons().get(p).setEnabled(false));
                throw new MyException("notImplemented");
        }
    }

    /**
     * Atnaujina parametrus antroje lentelėje. Taip pat tikrina ar pasikeitė
     * parametro reikšmė nuo praeito karto. Jei pasikeitė - spalvina jo reikšmę
     * raudonai
     *
     * @param colorize ar spalvinti parametrų reikšmes raudonai
     */
    private void updateHashtableParameters(boolean colorize) {
        String[] parameters = new String[]{
            String.valueOf(map.size()),
            String.valueOf(map.getTableCapacity()),
            String.valueOf(map.getMaxChainSize()),
            String.valueOf(map.getRehashesCounter()),
            String.valueOf(map.getLastUpdatedChain()),
            // Užimtų maišos lentelės elementų skaičius %            
            String.format("%3.2f", (double) map.getChainsCounter() / map.getTableCapacity() * 100) + "%"
        // .. naujus parametrus tęsiame čia ..
        };
        for (int i = 0; i < parameters.length; i++) {
            String str = panParam2.getTfOfTable().get(i).getText();
            if ((!str.equals(parameters[i]) && !str.equals("") && colorize)) {
                panParam2.getTfOfTable().get(i).setForeground(Color.RED);
            } else {
                panParam2.getTfOfTable().get(i).setForeground(Color.BLACK);
            }
            panParam2.getTfOfTable().get(i).setText(parameters[i]);
        }
    }

    public JTextArea getTaEvents() {
        return taEvents;
    }

    /**
     * Klasė, skirta JTextField objekte įvedamo skaičiaus tikrinimui. Tikrinama
     * ar įvestas neneigiamas skaičius
     */
    private class NotNegativeNumberVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            try {
                int result = Integer.valueOf(text);
                input.setBackground(result >= 0 ? Color.WHITE : Color.RED);
                return result >= 0;
            } catch (NumberFormatException e) {
                input.setBackground(Color.RED);
                return false;
            }
        }
    }

    public static void createAndShowGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName()
                // Arba sitaip, tada swing komponentu isvaizda priklausys
                // nuo naudojamos OS:
                // UIManager.getSystemLookAndFeelClassName()
                // Arba taip:
                //  "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
                // Linux'e dar taip:
                // "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
                );
                UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Ks.ou(ex.getMessage());
            }
            Lab3Window window = new Lab3Window();
            window.setIconImage(new ImageIcon(MESSAGES.getString("icon")).getImage());
            window.setTitle(MESSAGES.getString("title"));
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            //window.setPreferredSize(new Dimension(1000, 650));
            window.pack();
            window.setVisible(true);
        });
    }
}
