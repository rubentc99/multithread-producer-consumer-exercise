package Practica_2_ProductorConsumidor;
/*
CONSUMIDOR:
- Si hay tarta = Me como una
- Si no ... = Despertar COCINERO, dormir CONSUMIDOR.
COCINERO:
- Me duermo esperando a que un cliente me llame
- Si me llama produzco tarta, y me duermo.
*/
public class ProductorConsumidor implements Runnable {
 
     private boolean consumidor;
     private static int tarta=0;
     private static Object lock = new Object();
 
     public ProductorConsumidor(boolean consumidor) {
          this.consumidor=consumidor;
     }
 
     public void run() {
          while(true) {
                if(consumidor) {
                     consumiendo();
                }else {
                     cocinando();
                }
          }
     }
 
     private void consumiendo() {
          synchronized(lock) { //Cerrojo
                if(tarta>0) { //Si quedan tartas
                     tarta--; //Comer un trozo de tarta
                     System.out.println("Soy el consumidor y quiero un trozo de tarta.");
             		 System.out.println("Quedan " + tarta + " porciones de tarta.");
             		 System.out.println("----------------------------------------");
                     try {
                          Thread.sleep(1000);
                     } catch (InterruptedException e) {
                          e.printStackTrace();
                     }
                }else { //SI NO QUEDAN TARTAS...
                     lock.notifyAll(); //Despertar a el COCINERO que esperaba (abrir cerrojo, dejar pasar)
                     try {
                          lock.wait(); //Dormir al consumidor (hasta que lo despierten)
                     } catch (InterruptedException e) {
                          e.printStackTrace();
                     }
                }
          }
     }
 
     private void cocinando() {
          synchronized (lock) {
                if(tarta==0) {
                     tarta=(int)(Math.random()*10); //Cocinar un numero randomd de tarta entre 0 y 10
                     System.out.println("Soy el cocinero y voy a hacer una tarta de " + tarta + " porciones.");
             		 System.out.println("----------------------------------------");
                     lock.notifyAll(); //notifica que las tartas están listas
                } try {
                     lock.wait(); //dormir
                }catch(Exception e) {}
          }
     }
 
     public static void main(String[] args) {
          int numHilos = 11;
 
          Thread[] hilo = new Thread[numHilos];
 
          for(int i=0; i<hilo.length; i++) {
                Runnable runnable = null;
 
                if(i !=0) {
                     runnable = new ProductorConsumidor(true);
                }else {
                     runnable = new ProductorConsumidor(false);
                }
 
                hilo[i] = new Thread(runnable);
                hilo[i].start();
          }
 
          for(int i=0; i<hilo.length; i++) {
                try {
                     hilo[i].join();
                }catch(Exception e) {}
          }
     }
}