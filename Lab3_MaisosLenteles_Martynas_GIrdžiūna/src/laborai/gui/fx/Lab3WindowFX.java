package laborai.gui.fx;

import java.io.File;
import java.util.List;
import laborai.studijosktu.MapADTx;
import laborai.studijosktu.HashType;
import laborai.demo.Automobilis;
import laborai.demo.AutoGamyba;
import laborai.gui.MyException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import laborai.demo.GreitaveikosTyrimas;
import laborai.demo.MapKTUOAx;
import laborai.studijosktu.MapKTUx;

/**
 * Lab3 langas su JavaFX
 * <pre>
 *                    BorderLayout
 * |---------------------(Top)--------------------|
 * |                     menu                     |
 * |------------(Center)------------|---(Right)---|
 * |                                |--paneRight--|
 * |                                |             |
 * |                                |             |
 * |                                |    |
 * |             table              |             |
 * |                                |             |
 * |                                |             |
 * |                                |-------------|
 * |                                | paneButtons |
 * |--------------------------------|-------------|
 * |--------------------(Bottom)------------------|
 * |----------------panParam123Events-------------|
 * |-----------|-----------|-----------|----------|
 * | panParam1 | panParam2 | panParam3 | taEvents |
 * |-----------|-----------|-----------|----------|
 * </pre>
 *
 * @author darius.matulis@ktu.lt
 */
public class Lab3WindowFX extends BorderPane implements EventHandler<ActionEvent> {

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("laborai.gui.messages");
    private static final int TF_WIDTH = 200;
    private static final int TF_WIDTH_SMALLER = 80;
    private static final double SPACING = 5.0;
    private static final Insets INSETS = new Insets(SPACING);
    private static final double SPACING_SMALLER = 2.0;
    private static final Insets INSETS_SMALLER = new Insets(SPACING_SMALLER);

    private final ComboBox cmbCollisionTypes = new ComboBox();
    private final ComboBox cmbHashFunctions = new ComboBox();
    private final TextArea taInput = new TextArea();
    private final GridPane paneParam123Events = new GridPane();
    private final GridPane paneRight = new GridPane();
    private final TextArea taEvents = new TextArea();
    private PanelsFX paneParam1, paneParam2, paneParam3, paneButtons;
    private TableFX<String[], String> table;
    private MenuFX menuFX;
    private final Stage stage;

    private MapADTx<String, Automobilis> map;
    private int sizeOfInitialSubSet, sizeOfGenSet, colWidth, initialCapacity;
    private float loadFactor;
    private HashType ht = HashType.DIVISION;
    private final AutoGamyba autoGamyba = new AutoGamyba();

    public Lab3WindowFX(Stage stage) {
        this.stage = stage;
        initComponents();
    }

    private void initComponents() {
        // Formuojamas rožinės spalvos panelis (dešinėje pusėje)
        // Užpildomi ComboBox'ai
        cmbCollisionTypes.setItems(FXCollections.observableArrayList(
                new String[]{
                    MESSAGES.getString("cmbCollisionType1"),
                    MESSAGES.getString("cmbCollisionType2"),
                    MESSAGES.getString("cmbCollisionType3"),
                    MESSAGES.getString("cmbCollisionType4")}
        ));
        cmbCollisionTypes.setOnAction(this);
        cmbCollisionTypes.getSelectionModel().select(0);
        cmbHashFunctions.setItems(FXCollections.observableArrayList(
                new String[]{
                    MESSAGES.getString("cmbHashFunction1"),
                    MESSAGES.getString("cmbHashFunction2"),
                    MESSAGES.getString("cmbHashFunction3"),
                    MESSAGES.getString("cmbHashFunction4")}
        ));
        cmbHashFunctions.setOnAction(this);
        cmbHashFunctions.getSelectionModel().select(0);

        // Formuojamas mygtukų tinklelis (mėlynas). Naudojama klasė PanelsFX.
        paneButtons = new PanelsFX(
                new String[]{
                    MESSAGES.getString("button1"),
                    MESSAGES.getString("button2"),
                    MESSAGES.getString("button3"),
                    MESSAGES.getString("button4"),
                    MESSAGES.getString("button5"),
                    MESSAGES.getString("button6"),}, 1, 6);
        paneButtons.getButtons().forEach((btn) -> btn.setOnAction(this));
        IntStream.of(1, 3, 4, 5).forEach(p -> paneButtons.getButtons().get(p).setDisable(true));

        // Viskas sudedama į rožinės spalvos pane
        GridPane.setVgrow(taInput, Priority.ALWAYS);
        taInput.setPrefWidth(TF_WIDTH);
        cmbCollisionTypes.setPrefWidth(TF_WIDTH);
        cmbHashFunctions.setPrefWidth(TF_WIDTH);
        Stream.of(new Label(MESSAGES.getString("border1")),
                cmbCollisionTypes,
                new Label(MESSAGES.getString("border2")),
                cmbHashFunctions,
                new Label(MESSAGES.getString("border3")),
                taInput,
                paneButtons
        ).forEach(n -> paneRight.addColumn(0, n));
        paneRight.setHgap(SPACING);
        paneRight.setVgap(SPACING);
        paneRight.setPadding(INSETS);

        // Formuojama pirmoji parametrų lentelė (šviesiai žalia). Naudojama klasė PanelsFX.
        paneParam1 = new PanelsFX(
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
                    MESSAGES.getString("tfParam16")}, TF_WIDTH_SMALLER);

        // Formuojama antroji parametrų lentelė (gelsva). Naudojama klasė PanelsFX.
        paneParam2 = new PanelsFX(
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
                    MESSAGES.getString("tfParam26")}, TF_WIDTH_SMALLER);

        // Formuojama trečioji parametrų lentelė (šviesiai žalia). Naudojama klasė PanelsFX.           
        paneParam3 = new PanelsFX(
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
                    MESSAGES.getString("tfParam36")}, TF_WIDTH_SMALLER);

        // Visų trijų lentelių paneliai sudedami į šviesiai pilką panelį
        paneParam123Events.setPadding(new Insets(SPACING_SMALLER, SPACING, SPACING, SPACING));
        paneParam123Events.setVgap(SPACING_SMALLER);
        paneParam123Events.setHgap(SPACING);
        paneParam123Events.add(new Label(MESSAGES.getString("border4")), 0, 0);
        GridPane.setHgrow(taEvents, Priority.ALWAYS);
        paneParam123Events.addRow(1, paneParam1, paneParam2, paneParam3, taEvents);
        paneParam123Events.add(new Label(MESSAGES.getString("border6")), 3, 0);

        // Sukuriama lentelė, sukuriamas trūkstamas metodas
        table = new TableFX<String[], String>() {
            @Override
            public ObservableValue<String> returnValue(TableColumn.CellDataFeatures<String[], String> p) {
                int index = Integer.valueOf(p.getTableColumn().getId());
                return new SimpleStringProperty(index < p.getValue().length ? p.getValue()[index] : "");
            }
        };

        // Sukuriamas meniu
        menuFX = new MenuFX() {
            @Override
            public void handle(ActionEvent ae) {
                try {
                    Object source = ae.getSource();
                    KsFX.setFormatStartOfLine(true);
                    if (source.equals(menuFX.getMenus().get(0).getItems().get(0))) {
                        fileChooseMenu();
                    } else if (source.equals(menuFX.getMenus().get(0).getItems().get(1))) {
                        KsFX.ounerr(taEvents, MESSAGES.getString("notImplemented"));
                    } else if (source.equals(menuFX.getMenus().get(0).getItems().get(3))) {
                        System.exit(0);
                    } else if (source.equals(menuFX.getMenus().get(1).getItems().get(0))) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.initStyle(StageStyle.UTILITY);
                        alert.setTitle(MESSAGES.getString("menuItem21"));
                        alert.setHeaderText(MESSAGES.getString("author"));
                        alert.showAndWait();
                    }
                } catch (MyException e) {
                    KsFX.ounerr(taEvents, e.getMessage());
                } catch (Exception e) {
                    KsFX.ounerr(taEvents, MESSAGES.getString("systemError"));
                    e.printStackTrace(System.out);
                }
                KsFX.setFormatStartOfLine(false);
            }
        };

        // Formuojamas Lab3 langas
        // ..viršuje, dešinėje, centre ir apačioje talpiname objektus..
        setTop(menuFX);

        VBox vboxTable = new VBox();
        vboxTable.setPadding(INSETS_SMALLER);
        VBox.setVgrow(table, Priority.ALWAYS);
        vboxTable.getChildren().addAll(new Label(MESSAGES.getString("border5")), table);
        setRight(paneRight);
        setCenter(vboxTable);
        setBottom(paneParam123Events);
        appearance();
    }

    /**
     * Kosmetika
     */
    private void appearance() {
        cmbCollisionTypes.setStyle(PanelsFX.STYLE_COMMON);
        cmbHashFunctions.setStyle(PanelsFX.STYLE_COMMON);
        paneParam1.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        paneParam2.setBackground(new Background(new BackgroundFill(Color.KHAKI, CornerRadii.EMPTY, Insets.EMPTY)));
        paneParam3.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        paneRight.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
        paneButtons.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        taInput.setFont(Font.font("Monospaced", 12));
        taInput.setDisable(true);
        taEvents.setEditable(false);
        taEvents.setFont(Font.font("Monospaced", 12));
        // Antra parametrų lentelė (gelsva) bus neredaguojama
        paneParam2.getTfOfTable().forEach(c -> {
            c.setEditable(false);
            c.setMouseTransparent(true);
        });
    }

    @Override
    public void handle(ActionEvent event) {
        KsFX.setFormatStartOfLine(true);
        Platform.runLater(() -> {
            try {
                System.gc();
                System.gc();
                System.gc();
                Region region = (Region) taEvents.lookup(".content");
                region.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

                Object source = event.getSource();
                if (source instanceof Button) {
                    handleButtons(source);
                } else if (source instanceof ComboBox && (source.equals(cmbCollisionTypes) || source.equals(cmbHashFunctions))) {
                    IntStream.of(1, 3, 4, 5).forEach(p -> paneButtons.getButtons().get(p).setDisable(true));
                }
            } catch (MyException e) {
                KsFX.ounerr(taEvents, MESSAGES.getString(e.getMessage()), e.getValue());
            } catch (UnsupportedOperationException e) {
                KsFX.ounerr(taEvents, e.getMessage());
            } catch (Exception e) {
                KsFX.ounerr(taEvents, MESSAGES.getString("systemError"));
                e.printStackTrace(System.out);
            }
        });
    }

    private void handleButtons(Object source) {
        if (source.equals(paneButtons.getButtons().get(0))) {
            mapGeneration(null);
        } else if (source.equals(paneButtons.getButtons().get(1))) {
            mapAdd();
        } else if (source.equals(paneButtons.getButtons().get(2))) {
            mapEfficiency();
        } else if (source.equals(paneButtons.getButtons().get(3))) {
            mapKeySet();
        } else if (source.equals(paneButtons.getButtons().get(4))) {
            mapValues();
        } else if (source.equals(paneButtons.getButtons().get(5))) {
            mapEfficiencySecond();
        }
    }
    
    public void mapValues()
    {
        List<Automobilis> values = map.values();
        KsFX.ounArgs(taEvents, MESSAGES.getString("msg7"));
        for (Automobilis a : values)
        {
            KsFX.ounArgs(taEvents, a.toString());
        }
    }
    
    public void mapKeySet()
    {
        Set<String> keys = map.keySet();
        KsFX.ounArgs(taEvents, MESSAGES.getString("msg8"));
        for (String s : keys)
        {
            KsFX.ounArgs(taEvents, s);
        }
    }

    public void mapGeneration(String filePath) {
        // Išjungiami 2 ir 4 mygtukai
        IntStream.of(1, 3, 4, 5).forEach(p -> paneButtons.getButtons().get(p).setDisable(true));
        // Duomenų nuskaitymas iš parametrų lentelės (žalios)
        readMapParameters();
        // Sukuriamas tuščias atvaizdis priklausomai nuo kolizijų tipo
        createMap();
        // Jei failas nenurodytas - generuojami automobiliai ir talpinami atvaizdyje
        if (filePath == null) {
            Automobilis[] autoArray = autoGamyba.gamintiIrParduotiAutomobilius(sizeOfGenSet, sizeOfInitialSubSet);
            for (Automobilis a : autoArray) {
                map.put(
                        autoGamyba.gautiIsBazesAutoId(), //raktas
                        a);
            }
            KsFX.ounArgs(taEvents, MESSAGES.getString("msg1"), map.size());
        } else { // Jei failas nurodytas skaitoma iš failo
            map.load(filePath);
            if (map.isEmpty()) {
                KsFX.ounerr(taEvents, MESSAGES.getString("msg6"), filePath);
            } else {
                KsFX.ou(taEvents, MESSAGES.getString("msg5"), filePath);
            }
        }

        // Atvaizdis rodomas lentelėje
        table.formTable(map.getMaxChainSize() * 2 + 1, colWidth);
        String[][] modelList = map.getModelList(paneParam1.getTfOfTable().get(5).getText());
        table.getItems().clear();
        table.setItems(FXCollections.observableArrayList(modelList));
        // Atnaujinamai maišos lentelės parametrai (geltona lentelė)
        updateHashtableParameters(false);
        // Įjungiami 2 ir 4 mygtukai
        IntStream.of(1, 3, 4, 5).forEach(p -> paneButtons.getButtons().get(p).setDisable(false));
    }

    public void mapAdd() {
        Automobilis a = autoGamyba.parduotiAutomobili();
        map.put(
                autoGamyba.gautiIsBazesAutoId(), // Raktas
                a);
        table.formTable(map.getMaxChainSize() * 2 + 1, colWidth);
        String[][] modelList = map.getModelList(paneParam1.getTfOfTable().get(5).getText());
        table.setItems(FXCollections.observableArrayList(modelList));
        updateHashtableParameters(true);
        KsFX.oun(taEvents, a, MESSAGES.getString("msg2"));
    }

    public void mapEfficiency() {
        KsFX.oun(taEvents, "", MESSAGES.getString("msg3"));
        paneRight.setDisable(true);
        menuFX.setDisable(true);
        GreitaveikosTyrimas gt = new GreitaveikosTyrimas();

        // Ši gija paima rezultatus iš greitaveikos tyrimo gijos ir išveda 
        // juos i taEvents. Gija baigia darbą kai gaunama FINISH_COMMAND     
        new Thread(() -> {
            try {
                String result;
                while (!(result = gt.getResultsLogger().take())
                        .equals(GreitaveikosTyrimas.FINISH_COMMAND)) {
                    KsFX.oun(taEvents, result);
                    gt.getSemaphore().release();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            gt.getSemaphore().release();
            paneRight.setDisable(false);
            menuFX.setDisable(false);
        }, "Greitaveikos_rezultatu_gija").start();

        //Šioje gijoje atliekamas greitaveikos tyrimas
        new Thread(() -> gt.pradetiTyrima(), "Greitaveikos_tyrimo_gija").start();
    }
    
        public void mapEfficiencySecond() {
        KsFX.oun(taEvents, "", MESSAGES.getString("msg3"));
        paneRight.setDisable(true);
        menuFX.setDisable(true);
        GreitaveikosTyrimas gt = new GreitaveikosTyrimas();

        // Ši gija paima rezultatus iš greitaveikos tyrimo gijos ir išveda
        // juos i taEvents. Gija baigia darbą kai gaunama FINISH_COMMAND
        new Thread(() -> {
            try {
                String result;
                while (!(result = gt.getResultsLogger().take())
                        .equals(GreitaveikosTyrimas.FINISH_COMMAND)) {
                    KsFX.oun(taEvents, result);
                    gt.getSemaphore().release();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            gt.getSemaphore().release();
            paneRight.setDisable(false);
            menuFX.setDisable(false);
        }, "Greitaveikos_rezultatu_gija").start();

        //Šioje gijoje atliekamas greitaveikos tyrimas
        new Thread(() -> gt.startTest(), "Greitaveikos_tyrimo_gija").start();
    }

    private void readMapParameters() {
        int i = 0;
        List<TextField> tfs = paneParam1.getTfOfTable();

        sizeOfInitialSubSet = notNegativeNumberVerifier(tfs.get(i++), "badSizeOfInitialSubSet");
        sizeOfGenSet = notNegativeNumberVerifier(tfs.get(i++), "badSizeOfGenSet");
        initialCapacity = notNegativeNumberVerifier(tfs.get(i++), "badInitialCapacity");
        loadFactor = loadFactorVerifier(tfs.get(i++));
        colWidth = notNegativeNumberVerifier(tfs.get(i), "badColumnWidth");

        switch (cmbHashFunctions.getSelectionModel().getSelectedIndex()) {
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
        switch (cmbCollisionTypes.getSelectionModel().getSelectedIndex()) {
            case 0:
                map = new MapKTUx<>(new String(), new Automobilis(), initialCapacity, loadFactor, ht);
                break;
            case 2: 
                map = (MapADTx) new MapKTUOAx<>(initialCapacity, loadFactor, ht);
                break;
            // ...
            // Programuojant kitus kolizijų sprendimo metodus reikia papildyti switch sakinį
            default:
                IntStream.of(1, 3).forEach(p -> paneButtons.getButtons().get(p).setDisable(true));
                throw new MyException("notImplemented");
        }
    }

    /**
     * Atnaujina parametrus antroje lentelėje. Taip pat tikrina ar pasikeitė
     * parametro reikšmė nuo praeito karto. Jei pasikeitė - spalvina jo reikšmę
     * rožine spalva
     *
     * @param colorize ar spalvinti parametrų reikšmes rožine spalva
     */
    private void updateHashtableParameters(boolean colorize) {
        String[] parameters = new String[]{
            String.valueOf(map.size()),
            String.valueOf(map.getTableCapacity()),
            String.valueOf(map.getMaxChainSize()),
            String.valueOf(map.getRehashesCounter()),
            String.valueOf(map.getLastUpdatedChain()),
            // Užimtų maišos lentelės elementų skaičius procentais           
            String.format("%3.2f", (double) map.getChainsCounter() / map.getTableCapacity() * 100) + "%"
        // .. naujus parametrus tęsiame čia ..
        };
        for (int i = 0; i < parameters.length; i++) {
            String str = paneParam2.getTfOfTable().get(i).getText();
            if ((!str.equals(parameters[i]) && !str.equals("") && colorize)) {
                paneParam2.getTfOfTable().get(i).setStyle(PanelsFX.STYLE_PINK);
            } else {
                paneParam2.getTfOfTable().get(i).setStyle(PanelsFX.STYLE_COMMON);
            }
            paneParam2.getTfOfTable().get(i).setText(parameters[i]);
        }
    }

    private void fileChooseMenu() {
        FileChooser fc = new FileChooser();
        // Papildoma mūsų sukurtais filtrais
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("txt", "*.txt")
        );
        fc.setTitle((MESSAGES.getString("menuItem11")));
        fc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            Region region = (Region) taEvents.lookup(".content");
            region.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            mapGeneration(file.getAbsolutePath());
        }
    }

    /**
     * Skirtas TextField objekte įvedamo skaičiaus tikrinimui. Tikrinama ar
     * įvestas neneigiamas skaičius
     */
    private int notNegativeNumberVerifier(TextField tf, String errorMessage) {
        int result;
        try {
            result = Integer.valueOf(tf.getText());
            tf.setStyle(result < 0 ? PanelsFX.STYLE_ERROR : PanelsFX.STYLE_COMMON);
            if (result < 0) {
                throw new MyException(errorMessage, tf.getText());
            }
        } catch (NumberFormatException e) {
            tf.setStyle(PanelsFX.STYLE_ERROR);
            throw new MyException(errorMessage, tf.getText());
        }

        return result;
    }

    /**
     * Skirtas apkrovimo faktoriaus tikrinimui. Tikrinama ar įvestas skaičius
     * (0,1]
     */
    private float loadFactorVerifier(TextField tf) {
        float factor;
        try {
            factor = Float.valueOf(tf.getText());
            tf.setStyle(factor <= 0.0 || factor > 1.0 ? PanelsFX.STYLE_ERROR : PanelsFX.STYLE_COMMON);
            if (factor <= 0.0 || factor > 1.0) {
                throw new MyException("badLoadFactor", tf.getText());
            }
        } catch (NumberFormatException e) {
            tf.setStyle(PanelsFX.STYLE_ERROR);
            throw new MyException("badLoadFactor", tf.getText());
        }
        return factor;
    }

    public static void createAndShowFXGUI(Stage stage) {
        Platform.runLater(() -> {
            Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus
            Lab3WindowFX window = new Lab3WindowFX(stage);
            stage.setScene(new Scene(window));
            stage.setTitle(MESSAGES.getString("title"));
            stage.getIcons().add(new Image("file:" + MESSAGES.getString("icon")));
            stage.setOnCloseRequest((WindowEvent event) -> {
                System.exit(0);
            });
            //stage.setMaximized(true);         
            stage.show();
        });
    }
}
