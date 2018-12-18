package laborai.demo;

import laborai.studijosktu.MapKTUx;
import laborai.studijosktu.Ks;
import java.util.Locale;
import laborai.studijosktu.HashType;

public class AutoTestai {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US); // suvienodiname skaičių formatus
        //atvaizdzioTestas();
        greitaveikosTestas();
    }

    public static void atvaizdzioTestas() {
        Automobilis a1 = new Automobilis("Renault", "Laguna", 1997, 50000, 1700);
        Automobilis a2 = new Automobilis("Renault", "Megane", 2001, 20000, 3500);
        Automobilis a3 = new Automobilis("Toyota", "Corolla", 2001, 20000, 8500.8);
        Automobilis a4 = new Automobilis("Renault Laguna 2001 115900 7500");
        Automobilis a5 = new Automobilis.Builder().buildRandom();
        Automobilis a6 = new Automobilis("Honda   Civic  2007  36400 8500.3");
        Automobilis a7 = new Automobilis("Renault Laguna 2001 115900 7500");

        // Raktų masyvas
        String[] autoId = {"TA156", "TA102", "TA178", "TA171", "TA105", "TA106", "TA107", "TA108"};
        int id = 0;
        MapKTUx<String, Automobilis> atvaizdis
                = new MapKTUx(new String(), new Automobilis(), HashType.DIVISION);
        // Reikšmių masyvas
        Automobilis[] auto = {a1, a2, a3, a4, a5, a6, a7};
        for (Automobilis a : auto) {
            atvaizdis.put(autoId[id++], a);
        }
        atvaizdis.println("Porų išsidėstymas atvaizdyje pagal raktus");
        Ks.oun("Ar egzistuoja pora atvaizdyje?");
        Ks.oun(atvaizdis.contains(autoId[6]));
        Ks.oun(atvaizdis.contains(autoId[7]));
        Ks.oun("Pašalinamos poros iš atvaizdžio:");
        Ks.oun(atvaizdis.remove(autoId[1]));
        Ks.oun(atvaizdis.remove(autoId[7]));
        atvaizdis.println("Porų išsidėstymas atvaizdyje pagal raktus");
        Ks.oun("Atliekame porų paiešką atvaizdyje:");
        Ks.oun(atvaizdis.get(autoId[2]));
        Ks.oun(atvaizdis.get(autoId[7]));
        Ks.oun("ContainsValue metodas: ");
        Ks.oun(a1.toString());
        Ks.oun(atvaizdis.containsValue(a1));
        Ks.oun("numberOfEmpties metodas: ");
        Ks.oun(atvaizdis.numberOfEmpties());
        Ks.oun("Išspausdiname atvaizdžio poras String eilute:");
        Ks.ounn(atvaizdis);
    }

    //Konsoliniame režime
    private static void greitaveikosTestas() {
        System.out.println("Greitaveikos tyrimas:\n");
        GreitaveikosTyrimas gt = new GreitaveikosTyrimas();
        //Šioje gijoje atliekamas greitaveikos tyrimas
        new Thread(() -> gt.pradetiTyrima(),
                "Greitaveikos_tyrimo_gija").start();
        try {
            String result;
            while (!(result = gt.getResultsLogger().take())
                    .equals(GreitaveikosTyrimas.FINISH_COMMAND)) {
                System.out.println(result);
                gt.getSemaphore().release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        gt.getSemaphore().release();
    }
}