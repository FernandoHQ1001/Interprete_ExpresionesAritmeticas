/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt para cambiar esta licencia
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java para editar esta plantilla
 */
package Proyecto2;

public class Token {
    public enum Tipo {
        ID, NUM, MAS, MENOS, MUL, DIV, ASIGNAR, LPAREN, RPAREN, IMPRIMIR, EOF
    }

    public Tipo tipo;
    public String valor;

    public Token(Tipo tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
}
