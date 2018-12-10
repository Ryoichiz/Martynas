/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package circularbuffer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import laborai.gui.MyException;

/**
 *
 * @author Martynas
 */
public class GreitaveikosTyrimas {

    public static final String FINISH_COMMAND = "finish";
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("laborai.gui.messages");

    private static final String[] TYRIMU_VARDAI = {"addCB", "addTS", "addHS", "addAL", "removeCB", "removeTS", "removeHS", "removeAL"};
    private static final int[] TIRIAMI_KIEKIAI = {10000, 20000, 40000, 80000};

    private final BlockingQueue resultsLogger = new SynchronousQueue();
    private final Semaphore semaphore = new Semaphore(-1);
    private final Timekeeper tk;
    private final String[] errors;

    private final CircularBuffer<Integer> iSeries = new CircularBuffer(1000);
    private final TreeSet<Integer> iSeries2 = new TreeSet<>();
    private final HashSet<Integer> iSeries3 = new HashSet<>();
    private final ArrayList<Integer> collection = new ArrayList<>();
    

    public GreitaveikosTyrimas() {
        semaphore.release();
        tk = new Timekeeper(TIRIAMI_KIEKIAI, resultsLogger, semaphore);
        errors = new String[]{
            MESSAGES.getString("error1"),
            MESSAGES.getString("error2"),
            MESSAGES.getString("error3"),
            MESSAGES.getString("error4")
        };
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

    public void SisteminisTyrimas() throws InterruptedException {
        try {
            for (int k : TIRIAMI_KIEKIAI) {
                int[] intMas = new Random().ints(k, 0, k).toArray();
                iSeries.clear();
                iSeries2.clear();
                iSeries3.clear();
                collection.clear();
                int[] values = new Random().ints(10, 0, k).toArray();
                tk.startAfterPause();
                tk.start();
                for (int e : intMas) {
                    iSeries.push(e);
                }
                tk.finish(TYRIMU_VARDAI[0]);
                for (int e : intMas) {
                    iSeries2.add(e);
                }
                tk.finish(TYRIMU_VARDAI[1]);
                for (int e : intMas) {
                    iSeries3.add(e);
                }
                tk.finish(TYRIMU_VARDAI[2]);
                for (int e : intMas) {
                    collection.add(e);
                }
                tk.finish(TYRIMU_VARDAI[3]);
                while(!iSeries.isEmpty()){
                    iSeries.pop();
                }
                tk.finish(TYRIMU_VARDAI[4]);
                while(!iSeries2.isEmpty()){
                    iSeries2.removeAll(iSeries2);
                }
                tk.finish(TYRIMU_VARDAI[5]);
                while(!iSeries3.isEmpty()){
                    iSeries3.removeAll(iSeries3);
                }
                tk.finish(TYRIMU_VARDAI[6]);
                while(!collection.isEmpty()){
                    collection.removeAll(collection);
                }
                tk.finish(TYRIMU_VARDAI[7]);
                tk.seriesFinish();
            }
            tk.logResult(FINISH_COMMAND);
        } catch (MyException e) {
            if (e.getCode() >= 0 && e.getCode() <= 3) {
                tk.logResult(errors[e.getCode()] + ": " + e.getMessage());
            } else if (e.getCode() == 4) {
                tk.logResult(MESSAGES.getString("msg3"));
            } else {
                tk.logResult(e.getMessage());
            }
        }
    }

    public BlockingQueue<String> getResultsLogger() {
        return resultsLogger;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
