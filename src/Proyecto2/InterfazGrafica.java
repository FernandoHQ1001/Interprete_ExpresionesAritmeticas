package Proyecto2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class InterfazGrafica extends JFrame {
    private JTextArea codigoEntrada;
    private JTextArea resultadoTokens;
    private JTextArea resultadoEjecucion;
    private AnalizadorLexico analizadorLexico;
    private ParserLL1 analizadorSintactico;

    public InterfazGrafica() {
        setTitle("Intérprete de Lenguaje");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel de entrada de código
        JPanel panelEntrada = new JPanel(new BorderLayout());
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Código de Entrada"));
        codigoEntrada = new JTextArea(10, 50);  // Configuramos para 10 líneas y 50 columnas por defecto
        JScrollPane scrollEntrada = new JScrollPane(codigoEntrada);
        panelEntrada.add(scrollEntrada, BorderLayout.CENTER);
        
        // Panel de resultados
        JPanel panelResultados = new JPanel(new GridLayout(2, 1));
        
        // Panel de tokens
        JPanel panelTokens = new JPanel(new BorderLayout());
        panelTokens.setBorder(BorderFactory.createTitledBorder("Tokens Encontrados"));
        resultadoTokens = new JTextArea();
        resultadoTokens.setEditable(false);
        JScrollPane scrollTokens = new JScrollPane(resultadoTokens);
        panelTokens.add(scrollTokens, BorderLayout.CENTER);
        
        // Panel de ejecución
        JPanel panelEjecucion = new JPanel(new BorderLayout());
        panelEjecucion.setBorder(BorderFactory.createTitledBorder("Resultado de Ejecución"));
        resultadoEjecucion = new JTextArea();
        resultadoEjecucion.setEditable(false);
        JScrollPane scrollEjecucion = new JScrollPane(resultadoEjecucion);
        panelEjecucion.add(scrollEjecucion, BorderLayout.CENTER);
        
        panelResultados.add(panelTokens);
        panelResultados.add(panelEjecucion);
        
        // Botón de ejecutar
        JButton botonEjecutar = new JButton("Ejecutar");
        botonEjecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarCodigo();
            }
        });
        
        // Agregar componentes al panel principal
        panelPrincipal.add(panelEntrada, BorderLayout.NORTH);
        panelPrincipal.add(panelResultados, BorderLayout.CENTER);
        panelPrincipal.add(botonEjecutar, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }

    private void ejecutarCodigo() {
        String codigo = codigoEntrada.getText();
        analizadorLexico = new AnalizadorLexico(codigo);
        List<Token> tokens = analizadorLexico.tokenizar();
        
        // Mostrar tokens
        StringBuilder tokensResultado = new StringBuilder();
        for (Token token : tokens) {
            tokensResultado.append(token.tipo).append(": ").append(token.valor).append("\n");
        }
        resultadoTokens.setText(tokensResultado.toString());
        
        // Analizar y ejecutar
        analizadorSintactico = new ParserLL1(tokens);
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);
        System.setOut(newOut);

        try {
            analizadorSintactico.analizar();
            System.out.flush();
            String output = baos.toString();
            resultadoEjecucion.setText(output);
        } catch (Exception ex) {
            resultadoEjecucion.setText("Error: " + ex.getMessage());
        } finally {
            System.setOut(oldOut);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfazGrafica().setVisible(true);
            }
        });
    }
}
