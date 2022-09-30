/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorl3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tarci
 */
public class Lexico {
    private char[] conteudo;
    private int indiceConteudo;

    public Lexico(String caminhoCodigoFonte) {
        try {
            String conteudoStr;
            conteudoStr = new String(Files.readAllBytes(Paths.get(caminhoCodigoFonte)));
            this.conteudo = conteudoStr.toCharArray();
            this.indiceConteudo = 0;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Retorna próximo char
    private char nextChar() {
        return this.conteudo[this.indiceConteudo++];
    }

    // Verifica existe próximo char ou chegou ao final do código fonte
    private boolean hasNextChar() {
        return indiceConteudo < this.conteudo.length;
    }

    // Retrocede o índice que aponta para o "char da vez" em uma unidade
    private void back() {
        this.indiceConteudo--;
    }

    // Identificar se char é letra minúscula
    private boolean isLetra(char c) {
        return (c >= 'a') && (c <= 'z');
    }

    // Identificar se char é dígito
    private boolean isDigito(char c) {
        return (c >= '0') && (c <= '9');
    }

    // Método retorna próximo token válido ou retorna mensagem de erro.
    public Token nextToken() {
        Token token = null;
        char c;
        int estado = 0;

        StringBuffer lexema = new StringBuffer();
        while (this.hasNextChar()) {
            c = this.nextChar();
            switch (estado) {
                case 0:
                    if (c == ' ' || c == '\t' || c == '\n' || c == '\r') { // caracteres de espaço em branco ASCII
                                                                           // tradicionais
                        estado = 0;
                    } else if (this.isLetra(c)) {
                        lexema.append(c);
                        estado = 7;
                    } else if (this.isDigito(c)) {
                        lexema.append(c);
                        estado = 1;
                    } else if (c == ')' ||
                            c == '(' ||
                            c == '{' ||
                            c == '}' ||
                            c == ',' ||
                            c == ';') {
                        lexema.append(c);
                        estado = 15;
                    } else if (c == '$') {
                        lexema.append(c);
                        estado = 18;
                        this.back();
                    } else if (c == '<') {
                        lexema.append(c);
                        estado = 8;
                    } else if (c == '>') {
                        lexema.append(c);
                        estado = 11;
                    } else if (c == '=') {
                        lexema.append(c);
                        estado = 13;
                    } else if (c == '+' ||
                            c == '-' ||
                            c == '*' ||
                            c == '/' ||
                            c == '%') {
                        lexema.append(c);
                        estado = 16;
                    } else if ((int) c == 39) {
                        lexema.append(c);
                        estado = 4;
                    } else {
                        lexema.append(c);
                        throw new RuntimeException("Erro: token inválido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 1:
                    if (this.isDigito(c)) {
                        lexema.append(c);
                        estado = 1;
                    } else if (c == '.') {
                        lexema.append(c);
                        estado = 2;
                    } else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_INTEIRO);
                    }
                    break;
                case 2:
                    if (this.isDigito(c)) {
                        lexema.append(c);
                        estado = 3;
                    } else {
                        throw new RuntimeException("Erro: número float inválido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 3:
                    if (this.isDigito(c)) {
                        lexema.append(c);
                        estado = 3;
                    } else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_REAL);
                    }
                    break;
                case 4:
                    if (this.isDigito(c) || this.isLetra(c)) {
                        lexema.append(c);
                        estado = 5;
                    } else {
                        throw new RuntimeException("Erro: token inválido para tipo char \"" + lexema.toString() + "\"");
                    }
                    break;
                case 5:
                    if ((int) c == 39) {
                        lexema.append(c);
                        estado = 6;
                    } else {
                        throw new RuntimeException("Erro: token inválido para tipo char \"" + lexema.toString() + "\"");
                    }
                    break;
                case 6:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_CHAR);
                case 7:
                    if (this.isDigito(c) || this.isLetra(c)) {
                        lexema.append(c);
                        estado = 7;
                    } else if (lexema.toString().equalsIgnoreCase("if") || lexema.toString().equalsIgnoreCase("else")
                            || lexema.toString().equalsIgnoreCase("int") || lexema.toString().equalsIgnoreCase("float")
                            || lexema.toString().equalsIgnoreCase("char") || lexema.toString().equalsIgnoreCase("while")
                            || lexema.toString().equalsIgnoreCase("main")) {
                        estado = 17;
                    } else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_IDENTIFICADOR);
                    }
                    break;
                case 8:
                    if (c == '>') {
                        lexema.append(c);
                        estado = 9;
                    } else if (c == '=') {
                        lexema.append(c);
                        estado = 10;
                    } else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                    }
                    break;
                case 9:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                case 10:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                case 11:
                    if (c == '=') {
                        lexema.append(c);
                        estado = 12;
                    } else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                    }
                    break;
                case 12:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                case 13:
                    if (c == '=') {
                        lexema.append(c);
                        estado = 14;
                    } else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_ATRIBUICAO);
                    }
                    break;
                case 14:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                case 15:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_CARACTER_ESPECIAL);
                case 16:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_ARITMETICO);
                case 17:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_PALAVRA_RESERVADA);
                case 18:
                    return new Token(lexema.toString(), Token.TIPO_FIM_CODIGO);
            }
        }
        return token;
    }
}
