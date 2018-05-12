package Servidor;

import java.util.ArrayList;
import java.util.Observable;

public class ModeloServidor extends Observable {
    
    private ArrayList<Coordenada> serpiente;    //LA COLA ES EL ELEMENTO 0, Y LA CABEZA EL ÚLTIMO
    private ArrayList<Coordenada> tesoros;
    private int direccion;  //ARRIBA 1, ABAJO 2, IZQUIERDA 3, DERECHA 4
    private int puntos;
    
    public ModeloServidor(){
        serpiente = new ArrayList<>();
        Coordenada coord = new Coordenada(11,10);
        serpiente.add(coord);
        Coordenada coord2 = new Coordenada(10,10);
        serpiente.add(coord2);
        tesoros = new ArrayList<>();
        direccion = 0;
        puntos = 0;
    }
    
    //MÉTODOS
    public int getDireccion(){
        return direccion;
    }
    
    public void setDireccion(int i){
        direccion = i;
    }
    
    public int getPuntos(){
        return puntos;
    }
    
    public void setPuntos(int i){
        puntos = i;
    }
    
    public int getXCabeza(){
        return serpiente.get(serpiente.size()-1).x;
    }
    
    public int getYCabeza(){
        return serpiente.get(serpiente.size()-1).y;
    }
    
    public int getXSerpiente(int i){
        return serpiente.get(i).x;
    }
    
    public int getYSerpiente(int i){
        return serpiente.get(i).y;
    }
    
    public ArrayList<Coordenada> getSerpiente(){
        return serpiente;
    }
    
    public void borrarCola(){
        serpiente.remove(0);
    }
    
    public void anadirCabeza(){
        int y = serpiente.get(serpiente.size()-1).y;
        int x = serpiente.get(serpiente.size()-1).x;
        switch(direccion){
            case 1:
                y-=1;
                break;
            case 2:
                y+=1;
                break;
            case 3:
                x-=1;
                break;
            case 4:
                x+=1;
                break;
        }
        Coordenada coord = new Coordenada(y,x);
        serpiente.add(coord);
    }
    
    public ArrayList<Coordenada> getTesoros(){
        return tesoros;
    }
    
    public void borrarTesoro(int cy, int cx){
        for (int i=0; i<tesoros.size(); i++){
            if((tesoros.get(i).y==cy)&&(tesoros.get(i).x==cx)){
                tesoros.remove(i);
                break;
            }
        }
    }
    
    public void anadirTesoro(){
        boolean repetir = true;
        int ty = 0;
        int tx = 0;
        while(repetir){
            repetir = false;
            ty = ((int)Math.floor(Math.random()*(19+1)));
            tx = ((int)Math.floor(Math.random()*(19+1)));
            for(int i = 0; i<serpiente.size(); i++){
                if((serpiente.get(i).y==ty)&&(serpiente.get(i).x==tx))repetir = true;
            }
        }
        Coordenada coord = new Coordenada(ty,tx);
        tesoros.add(coord);
    }
    
    public void notificaMov() {
        this.setChanged();
        this.notifyObservers(new Integer(1));
    }
    
    public void notificaTes() {
        this.setChanged();
        this.notifyObservers(new Integer(2));
    }
    
    public void notificaPun() {
        this.setChanged();
        this.notifyObservers(new Integer(3));
    }
    
    public void notificaError() {
        this.setChanged();
        this.notifyObservers(new Integer(4));
    }
}
