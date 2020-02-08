/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mouserun.mouse;

import java.util.HashMap;
import java.util.Map;
import mouserun.game.*;

/**
 *
 * @author scloady
 * @version v2
 * @brief Clase M17B02 extendida de clase abstracta raton. Define un nuevo ratón
 */
public class M17B02 extends Mouse{
    
    /**
     * 
     * @brief Atributos de la clase principal
     */
    Map<Integer, Casilla> localizaciones= new HashMap<Integer,Casilla>();
    int casillaAnterior;
    
    /**
    * Notaciones frecuentes:
    * localizaciones.size() tamaño del mapa de elementos
    * localizaciones.isEmpty() Booleano, si es true no elementos
    * localizaciones.put(k,v) Añade al mapa una clave y un valor
    * localizaciones.get(k) devuelve el dato asociado al mapa
    * localizaciones.clear() limpia por completo el mapa
    * localizaciones.remove(k) elimina el par clave/dato asociado a la key dada
    * localizaciones.containsKey(k) devuelve un booleano si la clave esta o no en el mapa
    * localizaciones.values() devuelve una coleccion de los valores de todo el mapa
    * localizaciones.containsValue(v) devuelve un booleano si encuentra un valor coincidente con el pasado
    */
    
    /**
     * 
     * @brief Subclase casilla
     */
    public class Casilla {
        /**
         * 
         * @brief Atributos de la subclase casilla
         * CONTROLES: i inaccesible, v visitada, l libre, e entrada
         */
        char[] casillas = new char[5];

        /**
         * 
         * @brief Constructor por defecto de la subclase Casilla
         */
        public Casilla() {
            for(int i=0;i<casillas.length;i++){
                casillas[i]='i';
            }
        }
        
        /**
         * 
         * @brief Método set para poner la casilla como inaccesible
         * @param direccion Direccion por donde va el raton
         */
        public void setInaccesible(int direccion){
            casillas[direccion]='i';
        }
        
        /**
         * 
         * @brief Método set para poner la casilla como punto de entrada
         * @param direccion Direccion por donde va el raton
         */
        public void setEntrada(int direccion){
            casillas[direccion]='e';
        }
        
        /**
         * 
         * @brief Método set para poner la casilla como no visitada o libre
         * @param direccion Direccion por donde va el raton
         */
        public void setLibre(int direccion){
            casillas[direccion]='l';
        }
        
        /**
         * 
         * @brief Método set para poner la casilla como visitada
         * @param direccion Direccion por donde va el raton
         */
        public void setVisitada(int direccion){
            casillas[direccion]='v';
        }
        
        /**
         * 
         * @brief Método para seleccionar la dirección del ratón
         * @return Direccion a la que se va ir nuestro ratón
         */
        public int devuelveDireccion(){
            int posiDef=0;
            for(int i=0;i<casillas.length;i++){
                if(casillas[i]=='e'){
                    posiDef=i;
                }
                if(casillas[i]=='l'){
                    return i;
                }
            }
            return posiDef;
        }
        
        /**
         * 
         * @brief Método que devuelve el punto de entrada para la nueva casilla
         * @param direccion Direccion por donde iba el raton anteriormente
         * @return Punto de entrada de la nueva casilla
         */
        public int devuelveOpuesto(int direccion){
            if(direccion==1){
                return 2;
            }
            if(direccion==2){
                return 1;
            }
            if(direccion==3){
                return 4;
            }else{
                return 3;
            }
        }
        
        /**
         * 
         * @brief Método que indica si la direccion pasada como argumento es la de entrada
         * @param direccion Direccion por donde va el raton
         * @return Booleano que te dice si la direccion es la de entrada
         */
        public boolean esEntrada(int direccion){
            return casillas[direccion]=='e';
        }
        
    }
    
    /**
     * 
     * @brief Constructor de la clase principal M17B02
     */
    public M17B02() {
        super("Packman");
        casillaAnterior=0;
        localizaciones.clear();
    }
    /**
     * 
     * @brief Método que define el movimiento del ratón
     * @param currentGrid Casilla en la que nos encontramos actualmente
     * @param cheese Atributo que nos indica donde se localiza el queso
     * @return posicion a la que movemos el ratón
     */
    @Override
    public int move(Grid currentGrid, Cheese[] cheese) {
       //Primer paso: rellenamos la primera casilla para ir o las nuevas, segun se mire...
       if(localizaciones.isEmpty() || localizaciones.containsKey((currentGrid.getX()*100)+(currentGrid.getY()))==false){
           this.incExploredGrids();
           Casilla primeraCasilla=new Casilla();
           if(currentGrid.canGoDown()) primeraCasilla.setLibre(DOWN);
           if(currentGrid.canGoLeft()) primeraCasilla.setLibre(LEFT);
           if(currentGrid.canGoUp()) primeraCasilla.setLibre(UP);
           if(currentGrid.canGoRight()) primeraCasilla.setLibre(RIGHT);
           if(casillaAnterior!=0){
               primeraCasilla.setEntrada(primeraCasilla.devuelveOpuesto(casillaAnterior));
           }
           //Ahora obtenemos los parametros de direccion de la casilla y los concatenamos
           int key = (currentGrid.getX()*100)+(currentGrid.getY());
           //Insertamos...
           localizaciones.put(key, primeraCasilla);
       }
       
       //Segundo paso, nos vamos para un elemento
       Casilla miCasilla=localizaciones.get((currentGrid.getX()*100)+(currentGrid.getY()));
       //Preguntamos a nuestro sistema por donde vamos a ir...
       casillaAnterior=miCasilla.devuelveDireccion();
       //Ponemos esta direccion como visitada
       miCasilla.setVisitada(casillaAnterior);
       //Actualizamos el dato
       localizaciones.replace((currentGrid.getX()*100)+(currentGrid.getY()), miCasilla);
       //Vamos para alli
       return casillaAnterior;
    }
    
    /**
     * @brief Llamada cuando el raton alcanza el trozo de queso
     */
    @Override
    public void newCheese() { }
    
    /**
     * @brief Llamada cuando el raton pisa una bomba ajena
     */
    @Override
    public void respawned() { }
    
}
