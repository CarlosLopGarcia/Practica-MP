package Cliente;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import javax.swing.JOptionPane;

public class ControladorCliente implements KeyListener, MouseListener {
    
    //ATRIBUTOS
    private Socket socketC;
    private Conector conector;
    private Receptor rc;
    
    //CONTRUCTOR
    public ControladorCliente(String IP, int puerto){
        conector = new Conector(IP,puerto);
        PuntosCliente vpuntos = new PuntosCliente();
        vpuntos.setVisible(true);
        conector.addObserver(vpuntos);
        rc = new Receptor();
        rc.start();
    }
    
    public ControladorCliente(){
        PuntosCliente vpuntos = new PuntosCliente();
        vpuntos.setVisible(true);
    }
    
    //MÉTODOS
    public Conector getConector(){
        return this.conector;
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_UP) {conector.enviarMensaje("DIR;ARRIBA");}
        if (ke.getKeyCode() == KeyEvent.VK_DOWN) {conector.enviarMensaje("DIR;ABAJO");}
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {conector.enviarMensaje("DIR;IZQ");}
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {conector.enviarMensaje("DIR;DER");}
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        conector.enviarMensaje("ERR;");
        conector.closeS();
        System.exit(0);
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    //HEBRAS
    class Receptor extends Thread{
        @Override
        public void run(){
            while (true){
                try{
                    String mensaje = conector.recibirMensaje();
                    switch (mensaje.substring(0,3)){
                        case "MOV":
                            conector.setCabeza(Integer.parseInt(mensaje.substring(4,6)), Integer.parseInt(mensaje.substring(7,9)));
                            conector.setCola(Integer.parseInt(mensaje.substring(10,12)), Integer.parseInt(mensaje.substring(13)));
                            conector.notificaMov();
                            break;
                            
                        case "TES":
                            conector.sett(Integer.parseInt(mensaje.substring(4,6)), Integer.parseInt(mensaje.substring(7)));
                            conector.notificaTes();
                            break;
                            
                        case "PTS":
                            conector.setPuntos(Integer.parseInt(mensaje.substring(4)));
                            conector.notificaPuntos();
                            break;
                            
                        case "ERR":
                            conector.enviarMensaje("ERR;");
                            JOptionPane.showMessageDialog(null, "Te has chocado. Puntuacion: "+conector.getPuntos());
                            conector.closeS();
                            System.exit(0);
                            break;                           
                    }
                    Thread.sleep(0);
                }
                catch(Exception e){}
            }
        }
    }
    
    //SUBCLASE
    class Conector extends Observable{
        //ATRIBUTOS
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
        
        //CONSTRUCTOR
        public Conector(String IP, int puerto){
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
                conector.closeS();
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
    
}
