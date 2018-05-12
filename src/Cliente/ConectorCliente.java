package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import javax.swing.JOptionPane;

class ConectorCliente extends Observable{
    
    Socket socketCon;
    int puntuacion;
    int cay;
    int cax;
    int coy;
    int cox;
    int ty;
    int tx;
    PrintWriter write;
    InputStreamReader streamFromServer;
    BufferedReader serverInput;

    public ConectorCliente(String IP, int puerto){
        try{
            puntuacion = 0;
            this.socketCon = new Socket(IP,puerto);
            write = new PrintWriter(socketCon.getOutputStream(), true);
            streamFromServer=new InputStreamReader(socketCon.getInputStream());
            serverInput = new BufferedReader(streamFromServer);
            System.out.println("Conexión establecida");
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, "No se ha podido conectar con el servidor");
            System.exit(0);
        }
    }

    //MÉTODOS
    public String recibirMensaje(){
        try{
            String mensaje = serverInput.readLine();
            return mensaje;
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error en la conexión");
            this.closeS();
            System.exit(0);
        }
        return null;
    }

    public void closeS(){
        try{
            this.socketCon.close();
        }
        catch(Exception e){}
    }

    public void enviarMensaje(String mensaje){
        try{
            write.print(mensaje+'\n');
            write.flush();
        }
        catch(Exception e){}
    }

    public void setPuntos(int i){
        puntuacion = i;
    }

    public int getPuntos(){
        return puntuacion;
    }

    public void setCabeza(int y, int x){
        this.cay = y;
        this.cax = x;
    }

    public void setCola(int y, int x){
        this.coy = y;
        this.cox = x;
    }

    public int getCay(){
        return this.cay;
    }

    public int getCax(){
        return this.cax;
    }

    public int getCoy(){
        return this.coy;
    }

    public int getCox(){
        return this.cox;
    }

    public int getTy(){
        return this.ty;
    }

    public int getTx(){
        return this.tx;
    }

    public void sett(int y, int x){
        this.ty = y;
        this.tx = x;
    }

    public void notificaPuntos(){
        this.setChanged();
        this.notifyObservers(new Integer(1));
    }

    public void notificaMov(){
        this.setChanged();
        this.notifyObservers(new Integer(2));
    }

    public void notificaTes(){
        this.setChanged();
        this.notifyObservers(new Integer(3));
    }
}