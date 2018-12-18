package laborai.demo;

import laborai.studijosktu.Ks;
import laborai.studijosktu.KTUable;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/**
 * @author EK
 */
public final class Automobilis implements KTUable {

    // Bendri duomenys visiems automobiliams (visai klasei)
    private static final int priimtinųMetųRiba = 1990;
    private static final int esamiMetai = LocalDate.now().getYear();
    private static final double minKaina = 100.0;
    private static final double maxKaina = 333000.0;
    private String markė = "";
    private String modelis = "";
    private int gamMetai = -1;
    private int rida = -1;
    private double kaina = -1.0;

    public Automobilis() {
    }

    public Automobilis(String markė, String modelis,
            int gamMetai, int rida, double kaina) {
        this.markė = markė;
        this.modelis = modelis;
        this.gamMetai = gamMetai;
        this.rida = rida;
        this.kaina = kaina;
        validate();
    }

    public Automobilis(String e) {
        this.parse(e);
    }

    public Automobilis(Builder builder) {
        this.markė = builder.markė;
        this.modelis = builder.modelis;
        this.gamMetai = builder.gamMetai;
        this.rida = builder.rida;
        this.kaina = builder.kaina;
        validate();
    }

    @Override
    public Automobilis create(String dataString) {
        return new Automobilis(dataString);
    }

    @Override
    public String validate() {
        String klaidosTipas = "";
        assert (gamMetai < priimtinųMetųRiba || gamMetai > esamiMetai) :
                klaidosTipas = "Blogai nurodyti gamybos metai; ";
        assert (kaina < minKaina || kaina > maxKaina) :
                klaidosTipas += "Kaina už leistinų ribų; ";
        return klaidosTipas;
    }

    @Override
    public void parse(String dataString) {
        try {   // ed - tai elementarūs duomenys, atskirti tarpais
            Scanner ed = new Scanner(dataString);
            markė = ed.next();
            modelis = ed.next();
            gamMetai = ed.nextInt();
            setRida(ed.nextInt());
            setKaina(ed.nextDouble());
            validate();
        } catch (InputMismatchException e) {
            Ks.ern("Blogas duomenų formatas apie auto -> " + dataString);
        } catch (NoSuchElementException e) {
            Ks.ern("Trūksta duomenų apie auto -> " + dataString);
        }
    }

    @Override
    public String toString() {
        return markė + "_" + modelis + ":" + gamMetai + " " + getRida() + " "
                + String.format("%4.1f", kaina);
    }

    public String getMarkė() {
        return markė;
    }

    public String getModelis() {
        return modelis;
    }

    public int getGamMetai() {
        return gamMetai;
    }

    public int getRida() {
        return rida;
    }

    public double getKaina() {
        return kaina;
    }

    public void setKaina(double kaina) {
        this.kaina = kaina;
    }

    public void setRida(int rida) {
        this.rida = rida;
    }

    @Override
    public int hashCode() {
        return Objects.hash(markė, modelis, gamMetai, rida, kaina);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Automobilis other = (Automobilis) obj;
        if (!Objects.equals(this.markė, other.markė)) {
            return false;
        }
        if (!Objects.equals(this.modelis, other.modelis)) {
            return false;
        }
        if (this.gamMetai != other.gamMetai) {
            return false;
        }
        if (this.rida != other.rida) {
            return false;
        }
        if (Double.doubleToLongBits(this.kaina) != Double.doubleToLongBits(other.kaina)) {
            return false;
        }

        return true;
    }

    // Automobilis klases objektų gamintojas
    public static class Builder {

        private final static Random RANDOM = new Random(1949);  // Atsitiktinių generatorius
        private final static String[][] MODELIAI = { // galimų automobilių markių ir jų modelių masyvas
            {"Mazda", "3", "6", "CX-3", "MX-5"},
            {"Ford", "Fiesta", "Kuga", "Focus", "Galaxy", "Mondeo"},
            {"VW", "Golf", "Jetta", "Passat", "Tiguan"},
            {"Honda", "HR-V", "CR-V", "Civic", "Jazz"},
            {"Renault", "Clio", "Megane", "Twingo", "Scenic"},
            {"Peugeot", "208", "308", "508", "Partner"},
            {"Audi", "A3", "A4", "A6", "A8", "Q3", "Q5"}
        };

        private String markė = "";
        private String modelis = "";
        private int gamMetai = -1;
        private int rida = -1;
        private double kaina = -1.0;

        public Automobilis build() {
            return new Automobilis(this);
        }

        public Automobilis buildRandom() {
            int ma = RANDOM.nextInt(MODELIAI.length);        // markės indeksas  0..
            int mo = RANDOM.nextInt(MODELIAI[ma].length - 1) + 1;// modelio indeksas 1..              
            return new Automobilis(MODELIAI[ma][0],
                    MODELIAI[ma][mo],
                    1990 + RANDOM.nextInt(20),// metai tarp 1990 ir 2009
                    6000 + RANDOM.nextInt(222000),// rida tarp 6000 ir 228000
                    800 + RANDOM.nextDouble() * 88000);// kaina tarp 800 ir 88800
        }

        public Builder gamMetai(int gamMetai) {
            this.gamMetai = gamMetai;
            return this;
        }

        public Builder markė(String markė) {
            this.markė = markė;
            return this;
        }

        public Builder modelis(String modelis) {
            this.modelis = modelis;
            return this;
        }

        public Builder rida(int rida) {
            this.rida = rida;
            return this;
        }

        public Builder kaina(double kaina) {
            this.kaina = kaina;
            return this;
        }
    }
}
