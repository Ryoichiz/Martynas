package laborai.demo;

import laborai.studijosktu.HashType;
import laborai.studijosktu.MapKTUx;
import laborai.gui.MyException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.HashSet;
import java.util.HashMap;

/**
 * @author eimutis
 */
public class GreitaveikosTyrimas {

    public static final String FINISH_COMMAND = "finishCommand";
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("laborai.gui.messages");

    private final BlockingQueue resultsLogger = new SynchronousQueue();
    private final Semaphore semaphore = new Semaphore(-1);
    private final Timekeeper tk;

    private final String[] TYRIMU_VARDAI = {"add0.75", "add0.25", "rem0.75", "rem0.25", "get0.75", "get0.25"};
    private final String[] TEST_NAMES = {"KTU", "KTUOA", "HashSet"};
    private final int[] TIRIAMI_KIEKIAI = {10000, 20000, 40000, 80000};

    private final MapKTUx<String, Automobilis> autoAtvaizdis
            = new MapKTUx(new String(), new Automobilis(), 10, 0.75f, HashType.DIVISION);

    private final MapKTUOAx<String, Automobilis> autoAtvaizdisOA
            = new MapKTUOAx(new String(), new Automobilis(), 10, 0.75f, HashType.DIVISION);

    private final MapKTUx<String, Automobilis> autoAtvaizdis2
            = new MapKTUx(new String(), new Automobilis(), 10, 0.25f, HashType.DIVISION);

    private final HashMap<String, Automobilis> autoHash = new HashMap<>();

    private final Queue<String> chainsSizes = new LinkedList<>();

    public GreitaveikosTyrimas() {
        semaphore.release();
        tk = new Timekeeper(TIRIAMI_KIEKIAI, resultsLogger, semaphore);
    }

    public void pradetiTyrima() {
        try {
            SisteminisTyrimas();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void startTest() {
        try {
            speedTest();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void SisteminisTyrimas() throws InterruptedException {
        try {
            chainsSizes.add(MESSAGES.getString("msg4"));
            chainsSizes.add("   kiekis      " + TYRIMU_VARDAI[0] + "   " + TYRIMU_VARDAI[1]);
            for (int k : TIRIAMI_KIEKIAI) {
                Automobilis[] autoArray = AutoGamyba.gamintiAutomobilius(k);
                String[] autoIdArray = AutoGamyba.gamintiAutoIds(k);
                autoAtvaizdis.clear();
                autoAtvaizdis2.clear();
                tk.startAfterPause();
                tk.start();

                for (int i = 0; i < k; i++) {
                    autoAtvaizdis.put(autoIdArray[i], autoArray[i]);
                }
                tk.finish(TYRIMU_VARDAI[0]);

                String str = "   " + k + "          " + autoAtvaizdis.getMaxChainSize();
                for (int i = 0; i < k; i++) {
                    autoAtvaizdis2.put(autoIdArray[i], autoArray[i]);
                }
                tk.finish(TYRIMU_VARDAI[1]);

                str += "         " + autoAtvaizdis2.getMaxChainSize();
                chainsSizes.add(str);
                for (String s : autoIdArray) {
                    autoAtvaizdis.remove(s);
                }
                tk.finish(TYRIMU_VARDAI[2]);

                for (String s : autoIdArray) {
                    autoAtvaizdis2.remove(s);
                }
                tk.finish(TYRIMU_VARDAI[3]);

                for (String s : autoIdArray) {
                    autoAtvaizdis2.get(s);
                }
                tk.finish(TYRIMU_VARDAI[4]);

                for (String s : autoIdArray) {
                    autoAtvaizdis2.get(s);
                }
                tk.finish(TYRIMU_VARDAI[5]);
                tk.seriesFinish();
            }

            StringBuilder sb = new StringBuilder();
            chainsSizes.stream().forEach(p -> sb.append(p).append(System.lineSeparator()));
            tk.logResult(sb.toString());
            tk.logResult(FINISH_COMMAND);
        } catch (MyException e) {
            tk.logResult(e.getMessage());
        }
    }

    public void speedTest() throws InterruptedException {
        try {
            tk.logResult("Pavadinimai:     " + TEST_NAMES[0] + "      " + TEST_NAMES[1] + "     " + TEST_NAMES[1] + "    " + TEST_NAMES[2]);
            for (int k : TIRIAMI_KIEKIAI) {
                Automobilis[] autoArray = AutoGamyba.gamintiAutomobilius(k);
                String[] autoIdArray = AutoGamyba.gamintiAutoIds(k);
                autoAtvaizdis.clear();
                autoAtvaizdisOA.clear();
                for (int i = 0; i < k; i++) {
                    autoHash.put(autoIdArray[i], autoArray[i]);
                }
                tk.startAfterPause();
                tk.start();

                for (int i = 0; i < k; i++) {
                    autoAtvaizdis.put(autoIdArray[i], autoArray[i]);
                }
                tk.finish(TYRIMU_VARDAI[0]);

                String str = "   " + k + "          " + autoAtvaizdis.getMaxChainSize();
                for (int i = 0; i < k; i++) {
                    autoAtvaizdisOA.put(autoIdArray[i], autoArray[i]);
                }
                tk.finish(TYRIMU_VARDAI[1]);

                str += "         " + autoAtvaizdis2.getMaxChainSize();
                chainsSizes.add(str);
                for (String s : autoIdArray) {
                    autoAtvaizdisOA.remove(s);
                }
                tk.finish(TYRIMU_VARDAI[2]);

                for (String s : autoIdArray) {
                    autoHash.remove(s);
                }
                tk.finish(TYRIMU_VARDAI[3]);

                tk.seriesFinish();
            }

            tk.logResult(FINISH_COMMAND);
        } catch (MyException e) {
            tk.logResult(e.getMessage());
        }
    }

    public BlockingQueue<String> getResultsLogger() {
        return resultsLogger;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}