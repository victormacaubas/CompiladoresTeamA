package compiladorl3;

public class CompiladorL3 {

    public static void main(String[] args) {
        Lexico lexico = new Lexico("/workspace/CompiladoresTeamA/compilador-main/CompiladorL3/src/compiladorl3/codigo.txt");

        Sintatico1 sintatico = new Sintatico1(lexico);
        sintatico.S();
    }
}