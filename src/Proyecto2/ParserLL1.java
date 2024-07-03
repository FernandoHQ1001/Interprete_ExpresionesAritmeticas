/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt para cambiar esta licencia
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java para editar esta plantilla
 */
package Proyecto2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Clase para el análisis sintáctico (Parser LL(1))
import java.util.List;

public class ParserLL1 {
    private List<Token> tokens;
    private int posicion;
    private Token tokenActual;
    private TablaSimbolos tablaSimbolos;

    public ParserLL1(List<Token> tokens) {
        this.tokens = tokens;
        this.posicion = 0;
        this.tokenActual = tokens.get(posicion);
        this.tablaSimbolos = new TablaSimbolos();
    }

    private void avanzar() {
        posicion++;
        if (posicion < tokens.size()) {
            tokenActual = tokens.get(posicion);
        }
    }

    private void coincidir(Token.Tipo tipo) {
        if (tokenActual.tipo == tipo) {
            avanzar();
        } else {
            throw new RuntimeException("Token inesperado: " + tokenActual.valor);
        }
    }

    public void analizar() {
        while (tokenActual.tipo != Token.Tipo.EOF) {
            if (tokenActual.tipo == Token.Tipo.ID) {
                asignacion();
            } else if (tokenActual.tipo == Token.Tipo.IMPRIMIR) {
                sentenciaImprimir();
            } else {
                throw new RuntimeException("Declaración inesperada: " + tokenActual.valor);
            }
        }
    }

    private void asignacion() {
        String nombreVar = tokenActual.valor;
        coincidir(Token.Tipo.ID);
        coincidir(Token.Tipo.ASIGNAR);
        int valor = E();
        tablaSimbolos.put(nombreVar, valor);
    }

    private void sentenciaImprimir() {
        coincidir(Token.Tipo.IMPRIMIR);
        String nombreVar = tokenActual.valor;
        coincidir(Token.Tipo.ID);
        System.out.println(tablaSimbolos.get(nombreVar));
    }

    private int E() {
        int resultado = T();
        resultado = X(resultado);
        return resultado;
    }

    private int X(int izquierda) {
        switch (tokenActual.tipo) {
            case MAS:
                coincidir(Token.Tipo.MAS);
                izquierda += T();
                izquierda = X(izquierda);
                break;
            case MENOS:
                coincidir(Token.Tipo.MENOS);
                izquierda -= T();
                izquierda = X(izquierda);
                break;
            // Follow(E) casos
            case EOF:
            case RPAREN:
            case ID:
            case IMPRIMIR:
                break;
            default:
                throw new RuntimeException("Token inesperado en X: " + tokenActual.valor);
        }
        return izquierda;
    }

    private int T() {
        int resultado = F();
        resultado = Y(resultado);
        return resultado;
    }

    private int Y(int izquierda) {
        switch (tokenActual.tipo) {
            case MUL:
                coincidir(Token.Tipo.MUL);
                izquierda *= F();
                izquierda = Y(izquierda);
                break;
            case DIV:
                coincidir(Token.Tipo.DIV);
                izquierda /= F();
                izquierda = Y(izquierda);
                break;
            // Follow(T) casos
            case MAS:
            case MENOS:
            case EOF:
            case RPAREN:
            case ID:
            case IMPRIMIR:
                break;
            default:
                throw new RuntimeException("Token inesperado en Y: " + tokenActual.valor);
        }
        return izquierda;
    }

    private int F() {
        Token token = tokenActual;
        int resultado;
        switch (token.tipo) {
            case LPAREN:
                coincidir(Token.Tipo.LPAREN);
                resultado = E();
                coincidir(Token.Tipo.RPAREN);
                return resultado;
            case ID:
                coincidir(Token.Tipo.ID);
                return tablaSimbolos.get(token.valor);
            case NUM:
                coincidir(Token.Tipo.NUM);
                return Integer.parseInt(token.valor);
            default:
                throw new RuntimeException("Token inesperado en F: " + token.valor);
        }
    }
    
    /*
    public static void main(String[] args) {
        String input = "a = 5\nb = 3\nc = 2 + a * b + 4/2\nimprimir c\nc = c + 10\nimprimir c\na = a+1 \nimprimir a";
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        ParserLL1 parser = new ParserLL1(tokens);
        parser.parse();
    }
*/

    public static void main(String[] args) {
        List<Token> tokens = tokenizarDesdeArchivo("archivo.txt");
        ParserLL1 parser = new ParserLL1(tokens);
        parser.analizar();
    }

    public static List<Token> tokenizarDesdeArchivo(String nombreArchivo) {
        List<Token> tokens = null;
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
            AnalizadorLexico lexer = new AnalizadorLexico(sb.toString());
            tokens = lexer.tokenizar();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }
}
