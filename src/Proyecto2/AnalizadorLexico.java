/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt para cambiar esta licencia
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java para editar esta plantilla
 */
package Proyecto2;

// Clase para el análisis léxico (Lexer)

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
    private String fuente;
    private String entrada;
    private int posicion;
    private char caracterActual;

    public AnalizadorLexico(String entrada) {
        this.entrada = entrada;
        this.posicion = 0;
        this.caracterActual = entrada.charAt(posicion);
    }

    private void avanzar() {
        posicion++;
        if (posicion > entrada.length() - 1) {
            caracterActual = '\0';
        } else {
            caracterActual = entrada.charAt(posicion);
        }
    }

    private void omitirEspacios() {
        while (caracterActual != '\0' && Character.isWhitespace(caracterActual)) {
            avanzar();
        }
    }

    private String entero() {
        StringBuilder resultado = new StringBuilder();
        while (caracterActual != '\0' && Character.isDigit(caracterActual)) {
            resultado.append(caracterActual);
            avanzar();
        }
        return resultado.toString();
    }

    private String identificador() {
        StringBuilder resultado = new StringBuilder();
        while (caracterActual != '\0' && Character.isLetterOrDigit(caracterActual)) {
            resultado.append(caracterActual);
            avanzar();
        }
        return resultado.toString();
    }

    public List<Token> tokenizar() {
        List<Token> tokens = new ArrayList<>();
        while (caracterActual != '\0') {
            if (Character.isWhitespace(caracterActual)) {
                omitirEspacios();
                continue;
            }
            if (Character.isDigit(caracterActual)) {
                tokens.add(new Token(Token.Tipo.NUM, entero()));
                continue;
            }
            if (Character.isLetter(caracterActual)) {
                String id = identificador();
                if (id.equals("imprimir")) {
                    tokens.add(new Token(Token.Tipo.IMPRIMIR, id));
                } else {
                    tokens.add(new Token(Token.Tipo.ID, id));
                }
                continue;
            }
            switch (caracterActual) {
                case '+':
                    tokens.add(new Token(Token.Tipo.MAS, Character.toString(caracterActual)));
                    avanzar();
                    break;
                case '-':
                    tokens.add(new Token(Token.Tipo.MENOS, Character.toString(caracterActual)));
                    avanzar();
                    break;
                case '*':
                    tokens.add(new Token(Token.Tipo.MUL, Character.toString(caracterActual)));
                    avanzar();
                    break;
                case '/':
                    tokens.add(new Token(Token.Tipo.DIV, Character.toString(caracterActual)));
                    avanzar();
                    break;
                case '=':
                    tokens.add(new Token(Token.Tipo.ASIGNAR, Character.toString(caracterActual)));
                    avanzar();
                    break;
                case '(':
                    tokens.add(new Token(Token.Tipo.LPAREN, Character.toString(caracterActual)));
                    avanzar();
                    break;
                case ')':
                    tokens.add(new Token(Token.Tipo.RPAREN, Character.toString(caracterActual)));
                    avanzar();
                    break;
                default:
                    throw new RuntimeException("Caracter inesperado: " + caracterActual);
            }
        }
        tokens.add(new Token(Token.Tipo.EOF, ""));
        return tokens;
    }   
}
