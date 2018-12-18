package laborai.demo;

import laborai.gui.MyException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class AutoGamyba {

    private static final String ID_CODE = "TA";      //  ***** nauja
    private static int serNr = 10000;               //  ***** nauja

    private Automobilis[] automobiliai;
    private String[] raktai;
    private int kiekis = 0, idKiekis = 0;

    public static Automobilis[] gamintiAutomobilius(int kiekis) {
        Automobilis[] automobiliai = IntStream.range(0, kiekis)
                .mapToObj(i -> new Automobilis.Builder().buildRandom())
                .toArray(Automobilis[]::new);
        Collections.shuffle(Arrays.asList(automobiliai));
        return automobiliai;
    }

    public static String[] gamintiAutoIds(int kiekis) {
        String[] raktai = IntStream.range(0, kiekis)
                .mapToObj(i -> ID_CODE + (serNr++))
                .toArray(String[]::new);
        Collections.shuffle(Arrays.asList(raktai));
        return raktai;
    }

    public Automobilis[] gamintiIrParduotiAutomobilius(int aibesDydis,
            int aibesImtis) throws MyException {
        if (aibesImtis > aibesDydis) {
            aibesImtis = aibesDydis;
        }
        automobiliai = gamintiAutomobilius(aibesDydis);
        raktai = gamintiAutoIds(aibesDydis);
        this.kiekis = aibesImtis;
        return Arrays.copyOf(automobiliai, aibesImtis);
    }

    // Imamas po vienas elementas iš sugeneruoto masyvo. Kai elementai baigiasi sugeneruojama
    // nuosava situacija ir išmetamas pranešimas.
    public Automobilis parduotiAutomobili() {
        if (automobiliai == null) {
            throw new MyException("carsNotGenerated");
        }
        if (kiekis < automobiliai.length) {
            return automobiliai[kiekis++];
        } else {
            throw new MyException("allSetStoredToMap");
        }
    }

    public String gautiIsBazesAutoId() {
        if (raktai == null) {
            throw new MyException("carsIdsNotGenerated");
        }
        if (idKiekis >= raktai.length) {
            idKiekis = 0;
        }
        return raktai[idKiekis++];
    }
}