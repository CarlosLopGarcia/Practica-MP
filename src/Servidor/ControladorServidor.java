package Servidor;

public class ControladorServidor {
    
    private ModeloServidor modelo;
    private ConectorServidor conector;
    private CerrarServidor vCerrar;
    
    actualizarTablero at;
    Receptor rc;
    CTesoros ts;
    
    public ControladorServidor(ModeloServidor mod){
        this.modelo = mod;
        vCerrar = new CerrarServidor();
        vCerrar.setVisible(true);
        conector = new ConectorServidor(modelo);
        modelo.addObserver(conector);
        
        at = new actualizarTablero();
        rc = new Receptor();
        ts = new CTesoros();        
    }
    
    public void start() {
        this.at.start();
        this.rc.start();
        this.ts.start();
    }
    
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
                        //comer = true;
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
}
