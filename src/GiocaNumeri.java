/**
 * Classe principale che avvia il gioco con due giocatori in parallelo.
 */
public class GiocaNumeri {
    /**
     * Punto di ingresso del programma.
     *
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args)  {
        System.out.println("Benvenuto");


        Giocatore g1 = new Giocatore("gigi", 0);
        Giocatore g2 = new Giocatore("guizo", 0);

        Thread t = new Thread(g1);
        t.start();
        Thread t2 = new Thread(g2);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.err.println("Pausa interrotta: " + e.getMessage());
        }
        t2.start();

        try {
            t.join();
            t2.join();
        } catch (InterruptedException e) {
            System.err.println("transizione forzata");
        }

        System.out.println("Fine gioco!");
    }
}

