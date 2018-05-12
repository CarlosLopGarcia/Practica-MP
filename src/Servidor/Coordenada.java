package Servidor;

public class Coordenada{
    int y,x;

    public Coordenada(int y, int x){
        this.y = y;
        this.x = x;
    }

    public int getY(){
        return this.y;
    }

    public int getX(){
        return this.x;
    }

    public void setY(int i){
        this.y = i;
    }

    public void setX(int i){
        this.x = i;
    }
}