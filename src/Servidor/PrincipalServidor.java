package Servidor;
import javax.swing.JOptionPane;

public class PrincipalServidor {
    public static void main(String[] args) {
        System.out.println("SERVIDOR");
        ModeloServidor modeloM = new ModeloServidor();
        ControladorServidor controladorM = new ControladorServidor(modeloM);
    }
    
}