/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt para cambiar esta licencia
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java para editar esta plantilla
 */
package Proyecto2;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private Map<String, Integer> tabla;

    public TablaSimbolos() {
        tabla = new HashMap<>();
    }

    public void put(String nombre, int valor) {
        tabla.put(nombre, valor);
    }

    public int get(String nombre) {
        if (!tabla.containsKey(nombre)) {
            throw new RuntimeException("Variable no definida: " + nombre);
        }
        return tabla.get(nombre);
    }
}
