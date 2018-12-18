 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laborai.demo;

import laborai.studijosktu.MapADTx;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import laborai.studijosktu.HashType;
import laborai.studijosktu.Ks;



/**
 *
 * @author Martynas
 */
public class MapKTUOAx<K, V> extends MapKTUOA<K, V> implements MapADTx<K, V> {
            protected int rehashesCounter = 0;
    // Paskutinės patalpintos poros grandinėlės indeksas maišos lentelėje
    protected int lastUpdatedChain = 0;
    // Lentelės grandinėlių skaičius     
    protected int chainsCounter = 0;
    protected int average=0;
    private K baseKey;       // Bazinis objektas skirtas naujų kūrimui
    private V baseObj;       // Bazinis objektas skirtas naujų kūrimui

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     */
    public MapKTUOAx(K baseKey, V baseObj) {
        this(baseKey, baseObj, DEFAULT_HASH_TYPE);
    }

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     * @param ht
     */
    public MapKTUOAx(K baseKey, V baseObj, HashType ht) {
        this(baseKey, baseObj, DEFAULT_INITIAL_CAPACITY, ht);
    }

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     * @param initialCapacity
     * @param ht
     */
    public MapKTUOAx(K baseKey, V baseObj, int initialCapacity, HashType ht) {
        this(baseKey, baseObj, initialCapacity, DEFAULT_LOAD_FACTOR, ht);
    }

    /**
     * Konstruktorius su bazinio objekto fiksacija
     *
     * @param baseKey
     * @param baseObj
     * @param initialCapacity
     * @param loadFactor
     * @param ht
     */
    public MapKTUOAx(K baseKey, V baseObj, int initialCapacity, float loadFactor, HashType ht) {
        super(initialCapacity, loadFactor, ht);
        this.baseKey = baseKey;     // fiksacija dėl naujų elementų kūrimo
        this.baseObj = baseObj;     // fiksacija dėl naujų elementų kūrimo
    }

    @Override
    public V put(String dataString) {
        return put((K) dataString, (V) dataString);
    }

    @Override
    public void load(String filePath) {
        clear();
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        if ((baseKey == null) || (baseObj == null)) { // Elementų kūrimui reikalingas baseObj
            Ks.ern("Naudojant load-metodą, "
                    + "reikia taikyti konstruktorių = new MapKTU(new Data(),new Data())");
        }
        try {
            try (BufferedReader fReader = Files.newBufferedReader(Paths.get(filePath), Charset.defaultCharset())) {
                fReader.lines().filter(line -> line != null && !line.isEmpty()).forEach(line -> put(line));
            }
        } catch (FileNotFoundException e) {
            Ks.ern("Tinkamas duomenų failas " + filePath + " nerastas");
        } catch (IOException | UncheckedIOException e) {
            Ks.ern("Failo " + filePath + " skaitymo klaida:" + e.getLocalizedMessage());
        }
    }

    @Override
    public void save(String filePath) {
        throw new UnsupportedOperationException("save nepalaikomas");
    }

    @Override
    public void println() {
        if (super.isEmpty()) {
            Ks.oun("Atvaizdis yra tuščias");
        } else {
            String[][] data = getModelList("");
            for (int i = 0; i < data.length; i++) {
                Ks.oun(data[i][0] + "=" + data[i][1]);
            }
        }

        Ks.oufln("****** Bendras porų kiekis yra " + super.size());
    }

    @Override
    public void println(String title) {
        Ks.ounn("========" + title + "=======");
        println();
        Ks.ounn("======== Atvaizdžio pabaiga =======");
    }

    @Override
    public String[][] getModelList(String delimiter) {
        String[][] model = new String[getTableCapacity()][3];
        for (int i = 0; i < getTableCapacity(); i++) {
            model[i][0] = "[ " + i + " ]";
            if (table[i] == null) {
                model[i][1] = "";
                model[i][2] = "";
            } else {
                model[i][1] = table[i].key.toString();
                model[i][2] = table[i].value.toString();
            }
        }
        return model;
    }

    @Override
    public int getMaxChainSize() {
        return 1;
    }

    @Override
    public int getRehashesCounter() {
        return super.getRehashesCounter();
    }

    @Override
    public int getTableCapacity() {
        return super.getTableCapacity();
    }

    @Override
    public int getLastUpdatedChain() {
        return lastUpdatedChain;
    }

    @Override
    public int getChainsCounter() {
        return chainsCounter;
    }
}
