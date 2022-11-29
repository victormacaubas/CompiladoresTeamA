package compiladorl3;

import compiladorl3.Exepctions.SemanticException;
import compiladorl3.Exepctions.SintaticException;

public class CompiladorL3 {

    public static void main(String[] args) {
        try {


            Lexico lexico = new Lexico("src\\compiladorl3\\codigo.txt");

            Sintatico1 sintatico = new Sintatico1(lexico);
            sintatico.S();
        }
        catch (SintaticException exception) {
            System.out.println("ERRO SINTATICO " + exception.getMessage());
        }
        catch (SemanticException exception) {
            System.out.println("ERRO SEMANTICO " + exception.getMessage());
        }
        catch (RuntimeException exception) {
            System.out.println("ERRO LEXICO " + exception.getMessage());
        }
    }
}