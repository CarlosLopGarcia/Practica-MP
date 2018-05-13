package Servidor;

public class PrincipalServidor {
    public static void main(String[] args) {
        System.out.println("SERVIDOR INICIADO");
        ModeloServidor modeloM = new ModeloServidor();
        ControladorServidor controladorM = new ControladorServidor(modeloM);
    }
}