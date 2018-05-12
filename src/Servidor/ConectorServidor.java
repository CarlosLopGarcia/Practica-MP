package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Jherel
 */
public class ConectorServidor implements Observer {

    private ServerSocket socketS;
    private Socket socketCon;
    private InputStreamReader inputStream;
    private BufferedReader input;
    private PrintWriter write;
    private ModeloServidor modelo;
    
    private boolean comer;

    public ConectorServidor(ModeloServidor modelo){
        try{
            this.modelo = modelo;
            this.socketS = new ServerSocket(8000);
            socketCon = socketS.accept();
            inputStream = new InputStreamReader(socketCon.getInputStream());
            input = new BufferedReader(inputStream);
            write = new PrintWriter(socketCon.getOutputStream(), true);
            System.out.println("Conexión establecida");
        }
        catch(IOException e){}
    }

    //MÉTODOS
    public String recibirMensaje(){
        try{
            String mensaje = input.readLine();
            return mensaje;
        }
        catch(Exception e){}
        
        return null;
    }

    public void closeS(){
        try{
            this.socketS.close();
            this.socketCon.close();
        }
        catch(Exception e){}
    }

    public void enviarMensaje(String x){
        write.print(x);
        write.flush();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals(1)){
            String cay = "0";
            String cax = "0";
            String coy = "0";
            String cox = "0";
            if(modelo.getYCabeza()>9) cay = Integer.toString(modelo.getYCabeza());
            else cay += Integer.toString(modelo.getYCabeza());
            if(modelo.getXCabeza()>9) cax = Integer.toString(modelo.getXCabeza());
            else cax += Integer.toString(modelo.getXCabeza());
            if(modelo.getYSerpiente(0)>9) coy =  Integer.toString(modelo.getYSerpiente(0));
            else coy += Integer.toString(modelo.getYSerpiente(0));
            if(modelo.getXSerpiente(0)>9) cox =  Integer.toString(modelo.getXSerpiente(0));
            else cox += Integer.toString(modelo.getXSerpiente(0));
            if(comer == false){
                modelo.borrarCola();
                enviarMensaje("MOV;"+cay+";"+cax+";"+coy+";"+cox+'\n');
            }
            else{
                enviarMensaje("MOV;"+cay+";"+cax+";"+20+";"+20+'\n');
                comer = false;
            }
        }
        if(arg.equals(2)){
            String ty = "0";
            String tx = "0";
            if(modelo.getTesoros().get(modelo.getTesoros().size()-1).y>9) ty = Integer.toString(modelo.getTesoros().get(modelo.getTesoros().size()-1).y);
            else ty += Integer.toString(modelo.getTesoros().get(modelo.getTesoros().size()-1).y);
            if(modelo.getTesoros().get(modelo.getTesoros().size()-1).x>9) tx = Integer.toString(modelo.getTesoros().get(modelo.getTesoros().size()-1).x);
            else tx += Integer.toString(modelo.getTesoros().get(modelo.getTesoros().size()-1).x);
            enviarMensaje("TES;"+ty+";"+tx+'\n');
        }
        if(arg.equals(3)){
            enviarMensaje("PTS;"+modelo.getPuntos()+'\n');
        }
        if(arg.equals(4)){
            enviarMensaje("ERR;"+'\n');
        }
    }
}