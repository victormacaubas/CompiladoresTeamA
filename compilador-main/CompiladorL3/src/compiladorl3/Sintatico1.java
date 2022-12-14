package compiladorl3;

import java.util.ArrayList;

import compiladorl3.Exepctions.SemanticException;
import compiladorl3.Exepctions.SintaticException;

public class Sintatico1 {

    private Lexico lexico;
    private Token token;
    private int valueStored;
    private ArrayList<String> idlList;

    public Sintatico1(Lexico lexico) {
        this.lexico = lexico;
        this.idlList = new ArrayList<>();
    }

    public void S() {
        this.token = this.lexico.nextToken();

        if (!this.token.getLexema().equals("int")) {
            throw new SintaticException("O int deve ser no inicio");
        }

        this.token = this.lexico.nextToken();

        if (!this.token.getLexema().equals("main")) {
            throw new SintaticException("Coloque a main");
        }

        this.token = this.lexico.nextToken();

        if (!this.token.getLexema().equals("(")) {
            throw new SintaticException("Abrir o parenteses do main");
        }

        this.token = this.lexico.nextToken();

        if (!this.token.getLexema().equals(")")) {
            throw new SintaticException("Fechar o parenteses do main");
        }

        this.token = this.lexico.nextToken();

        this.Bloco();

        if (this.token.getTipo() == Token.TIPO_FIM_CODIGO) {
            System.out.println("O Código está massa! Arretado! Tu botou pra torar");
        }
    }

    private void Bloco() {

        if (!this.token.getLexema().equals("{")) {
            throw new SintaticException("É necessesario abrir chaves => { Encontramos => " + this.token.getLexema().toString());
        }

        this.token = this.lexico.nextToken();

        DecVariable();

        while (this.token.getTipo() == Token.TIPO_IDENTIFICADOR || this.token.getLexema().equals("{") || this.token.getLexema().equals("while") || this.token.getLexema().equals("if")) {
            this.Comando();
            
        }

        if (!token.getLexema().equals("}")) {
            throw new SintaticException("É necessesario fechar as chaves => { Encontramos => " + this.token.getLexema().toString());
        }

        this.token = this.lexico.nextToken();


    }

    private void Comando() {
        if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR || this.token.getLexema().equals("{")) {
            this.ComandoBasico();

        } else if (this.token.getLexema().equals("while")) {
            this.Iterations();

        } else if (this.token.getLexema().equals("if")) {
            this.token = this.lexico.nextToken();

            if (!this.token.getLexema().equals("(")) {
                throw new SintaticException(
                        "( esperado para finalizar a declaração, antes de =>  " + this.token.getLexema().toString());
            }

            this.token = this.lexico.nextToken();
            this.relationalExp();

            if (!this.token.getLexema().equals(")")) {
                throw new SintaticException(
                        ") esperado para finalizar a declaração, ao lado de =>  " + this.token.getLexema().toString());
            }

            this.token = this.lexico.nextToken();
            this.Comando();

            if (token.getLexema().equals("else")) {
                this.token = this.lexico.nextToken();
                this.Comando();
            } else {

            }
        } else {
            
        }

    }

    private void ComandoBasico() {

        if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
            this.Attr();
        }

        if (this.token.getLexema().equals("{")) {
            this.Bloco();
        }

    }

    private void Attr() {

        if (this.token.getTipo() != Token.TIPO_IDENTIFICADOR) {
            throw new SintaticException(
                    "é necessário declarar uma variável, foi encontrado => " + this.token.getLexema().toString());

        }
        this.searchId(this.token.getLexema());

        this.token = this.lexico.nextToken();

        if (!this.token.getLexema().equals("=")) {
            throw new SintaticException(
                    "= esperado para atribuir um valor, foi encontrado => " + this.token.getLexema().toString());

        }

        this.token = this.lexico.nextToken();

        this.aritmeticExp();

        if (!this.token.getLexema().equals(";")) {
            throw new SintaticException(
                    "; esperado para finalizar a declaração, erro proximo ao elemento => " + this.token.getLexema().toString());
        }

        this.token = this.lexico.nextToken();

    }

    private void Iterations() {

        if (!this.token.getLexema().equals("while")) {
            throw new SintaticException("while esperado, foi encontrado => " + this.token.getLexema().toString());
        }

        this.token = this.lexico.nextToken();

        if (!this.token.getLexema().equals("(")) {
            throw new SintaticException("( esperado para finalizar a declaração, ao lado de =>  " + this.token.getLexema().toString());
        
        }

        this.token = this.lexico.nextToken();

        this.relationalExp();

        if (!this.token.getLexema().equals(")")) {
            throw new SintaticException(") esperado para finalizar a declaração, ao lado de =>  " + this.token.getLexema().toString());
        
        }

        this.token = this.lexico.nextToken();
        this.Comando();


    }

    private void relationalExp() {
        this.aritmeticExp();

        if (this.token.getTipo() != Token.TIPO_OPERADOR_RELACIONAL) {
            throw new SintaticException(
                    "operador relacional esperado, foi encontrado =>  " + this.token.getLexema().toString());
        }

        this.token = this.lexico.nextToken();

        this.aritmeticExp();
    }

    private void aritmeticExp() {
        this.termo();
        this.e_Linha();
    }

    private void e_Linha() {

        if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO) {
            if (token.getLexema().equals("+")) {
                this.token = this.lexico.nextToken();

            } else if (this.token.getLexema().equals("-")) {
                this.token = this.lexico.nextToken();

            } else {
                throw new SintaticException("esperado um operador aritmetico, ao lado de =>  " + this.token.getLexema().toString());
        
            }
            this.termo();
            this.e_Linha();
        }
    }

    private void termo() {
        this.fator();
        this.termoMulti();
    }

    private void termoMulti() {

        if (this.token.getLexema().equals("*") || this.token.getLexema().equals("/")) {
            if (this.token.getLexema().equals("*")) {
                this.token = this.lexico.nextToken();
            } else if (this.token.getLexema().equals("/")) {
                this.token = this.lexico.nextToken();
            } else {
                throw new SintaticException ("Operador * ou / esperado, foi encontrado => " + this.token.getLexema().toString());
            }

        }
        /*
        if (this.token.getLexema().equals(";")) {
            this.token = this.lexico.nextToken();
        }*/

        //this.fator();
        //this.termo();
    }

    private void fator() {
        
        if (token.getLexema().equals("(")) {
            this.token = this.lexico.nextToken();
            this.aritmeticExp();

            if (!token.getLexema().equals(")")) {
                throw new SintaticException(") esperado para finalizar expressão, ao lado de =>  " + this.token.getLexema().toString());
            }

            this.token = this.lexico.nextToken();
            
        }

        if (this.token.getTipo() == 0 || this.token.getTipo() == 1 || this.token.getTipo() == 2 || this.token.getTipo() == 3) {
            this.token = this.lexico.nextToken();
        } else {
            throw new SintaticException("Identificador, float, int ou char são esperados, foi encontrado => " + this.token.getLexema());
        }
    }

    private void DecVariable() {

        if (this.token.getLexema().equals("int") || this.token.getLexema().equals("float")
        || this.token.getLexema().equals("char")) {

            if (this.token.getLexema().equals("int") || this.token.getLexema().equals("float")
                    || this.token.getLexema().equals("char")) {
                this.token = this.lexico.nextToken();

            } else {
                throw new SintaticException("Necessario informar o tipo da id (int, float, char) Encontramos => "
                        + this.token.getLexema().toString());
            }

            if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
                this.storeVariable(this.token.getLexema());
                this.token = this.lexico.nextToken();
            } else {
                throw new SintaticException(
                        "Identificador é esperado aqui, Encontramos => " + this.token.getLexema().toString());
            }

            if (this.token.getLexema().equals(";")) {
                this.token = this.lexico.nextToken();
            } else {
                throw new SintaticException(
                        "; esperado para finalizar a declaração, ao lado de => " + this.token.getLexema().toString());
            }
            this.DecVariable();

        } else {
            
        }

    }

    private void storeVariable(String id) {

        for (int i = 0; i < this.idlList.size(); i++) {
            if (id.equals(idlList.get(i))) {

                throw new SemanticException("Variável duplicada => " + this.token.getLexema().toString());
            }
        }

        this.idlList.add(id);
    }

    private void searchId(String id) {

        if (this.idlList.isEmpty()) {
            throw new SemanticException("Necessário declarar uma variável " + this.token.getLexema().toString());
        }

        for (int i = 0; i < this.idlList.size(); i++) {
            if (id.equals(idlList.get(i))) {
                valueStored = 1;
            }
        }

        if (valueStored != 1) {
            throw new SemanticException("Necessário declarar uma variável " + this.token.getLexema().toString());
        }
        
    }
}