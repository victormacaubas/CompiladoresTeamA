package compiladorl3;

public class CompiladorL3 {

    public static void main(String[] args) {
        Lexico lexico = new Lexico("src\\compiladorl3\\codigo.txt");

        Sintatico1 sintatico = new Sintatico1(lexico);
        sintatico.S();
    }
}