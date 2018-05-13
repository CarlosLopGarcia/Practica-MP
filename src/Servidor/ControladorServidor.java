package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class ControladorServidor {
    //ATRIBUTOS
    private ModeloServidor modelo;
    private ConectorServidor conector;
    private boolean comer;
    private VistaServidor vCerrar;
    
    //CONSTRUCTOR
    public ControladorServidor(ModeloServidor mod){
        this.modelo = mod;
        vCerrar = new VistaServidor();
        vCerrar.setVisible(true);
        conector = new ConectorServidor();
        modelo.addObserver(conector);
        actualizarTablero at = new actualizarTablero();
        Receptor rc = new Receptor();
        CTesoros ts = new CTesoros();
        at.start();
        rc.start();
        ts.start();
    }
    //MÉTODOS
    public boolean chocar(){
        if((modelo.getXCabeza()<0)||(modelo.getXCabeza()>19)||(modelo.getYCabeza()<0)||(modelo.getYCabeza()>19)) return true;
        for (int i=0; i<modelo.getSerpiente().size()-1; i++){
            if((modelo.getYSerpiente(i)==modelo.getYCabeza())&&(modelo.getXSerpiente(i)==modelo.getXCabeza()))return true;
        }
        return false;
    }
    
    public boolean cogerTesoro(){
        for(int i=0; i<modelo.getTesoros().size(); i++){
            if((modelo.getYCabeza()==modelo.getTesoros().get(i).getY())&&(modelo.getXCabeza()==modelo.getTesoros().get(i).getX())){
                modelo.borrarTesoro(modelo.getYCabeza(), modelo.getXCabeza());
                return true;
            }
        }
        return false;
    }
    
    public void reiniciar(){
        conector.closeS();
        vCerrar.setVisible(false);
        PrincipalServidor.main(null);
    }
    
    //HEBRAS
    class actualizarTablero extends Thread{
        @Override
        public void run(){
            while (true){
                try{
                    if(chocar()) modelo.notificaError();
                    if(modelo.getDireccion()!=0){
                        modelo.anadirCabeza();
                        modelo.notificaMov();
                    }
                    if(cogerTesoro()){
                        modelo.setPuntos(modelo.getPuntos()+1);
                        comer = true;
                        modelo.notificaPun();
                    }
                    Thread.sleep(100);
                }
                catch(Exception e){}
            }
        }
    }
    
    class CTesoros extends Thread{
        @Override
        public void run(){
            try{Thread.sleep(2000);}
            catch(Exception e){}
            try{
                while (true){
                    modelo.anadirTesoro();
                    modelo.notificaTes();
                    Thread.sleep(((int)Math.floor(Math.random()*(3000+1))));
                }
            }
            catch(Exception e){}
        }
    }
    
    class Receptor extends Thread{
        public void run(){
            while (true){
                try{
                    String mensaje = conector.recibirMensaje();
                    switch (mensaje.substring(0,3)){
                        case "DIR":
                            if(mensaje.substring(4).equals("ARRIBA")){
                                if(modelo.getDireccion()!=2) modelo.setDireccion(1);
                            }
                            if(mensaje.substring(4).equals("ABAJO")){
                                if(modelo.getDireccion()!=1) modelo.setDireccion(2);
                            }
                            if(mensaje.substring(4).equals("IZQ")){
                                if(modelo.getDireccion()!=4) modelo.setDireccion(3);
                            }
                            if(mensaje.substring(4).equals("DER")){
                                if(modelo.getDireccion()!=3) modelo.setDireccion(4);
                            }
                            break;
                            
                        case "ERR":
                            reiniciar();
                            break;
                    }
                    Thread.sleep(0);
                }
                catch(Exception e){System.out.println("ERROR AT");}
            }
        }
    }
    
    //SUBCLASE CONECTOR
    private class ConectorServidor implements Observer{
        //ATRIBUTOS
        private ServerSocket socketS;
        private Socket socketCon;
        private InputStreamReader inputStream;
        private BufferedReader input;
        PrintWriter write;
        
        //CONSTRUCTOR
        public ConectorServidor(){
            try{
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
            catch(Exception e){reiniciar();}
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
                if(comer==false){
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
}
