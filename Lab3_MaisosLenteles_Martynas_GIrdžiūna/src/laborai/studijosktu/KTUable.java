package laborai.studijosktu;

/** @author Eimutis Karčiauskas, KTU IF Programų inžinerijos katedra
 *
 * Tai yra  interfeisas, kurį turi tenkinti KTU studentų kuriamos duomenų klasės
 *       Metodai užtikrina patogų duomenų suformavimą iš String eilučių
 ******************************************************************************/
public interface KTUable {

    /**
     * Sukuria naują objektą iš teksto eilutės
     *
     * @param dataString
     * @return
     */
    KTUable create(String dataString);

    /**
     * Patikrina objekto reikšmes
     *
     * @return
     */
    String validate();

    /**
     * Suformuoja objektą iš teksto eilutės
     *
     * @param e
     */
    void parse(String e);
    /**
     * Atvaizduoja objektą į String eilutę
     *
     * @return
     */
    @Override
    String toString();
}