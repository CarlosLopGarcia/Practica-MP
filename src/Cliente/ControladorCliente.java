package Cliente;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ControladorCliente implements KeyListener, MouseListener {

    private Socket socketC;
    private ConectorCliente conector;
    private Receptor rc;
    
    public ControladorCliente(String IP, int puerto){
        conector = new ConectorCliente(IP,puerto);
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
    public ConectorCliente getConector(){
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
}
