/**
 * Rappresenta un giocatore del gioco che esegue le proprie azioni in un thread separato.
 */
public class Giocatore implements Runnable {

    /** Nome del giocatore. */
    String nome;
    /** Punteggio attuale del giocatore. */
    int punteggio;
    /** Parola scelta dal giocatore durante il gioco. */
    String parola;

    /**
     * Crea un nuovo giocatore.
     *
     * @param nome nome del giocatore
     * @param punteggio punteggio iniziale del giocatore
     */
    public Giocatore(String nome, int punteggio) {
        this.nome = nome;
        this.punteggio = punteggio;

    }

    /**
     * Esegue il thread del giocatore: gioca e poi comunica il risultato.
     */
    @Override                                                    
    public void run() {
        Thread thread = Thread.currentThread();
        long inizio = System.nanoTime();
        System.out.printf("Thread %s: id=%d, priorità=%d, stato=%s%n",
                thread.getName(), thread.getId(), thread.getPriority(), thread.getState());
        try {
            gioca();
            comunica();
        } finally {
            long durata = System.nanoTime() - inizio;
            System.out.printf("Tempo giocatore %s: %.3f ms%n", nome, durata / 1_000_000.0);
        }
    }

    /**
     * Esegue la giocata: stampa i numeri da 0 a 18, cedendo a ogni
     * iterazione il turno con {@code yield}, imposta la parola scelta
     * e aggiorna il punteggio a 100.
     */
    public void gioca() {
        int numero = 19;
        parola = "Buongiorno";

        for (int i = 0; i < numero; i++) {
            System.out.println("Giocatore " + nome + ": " + i);
            Thread.yield();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.err.println("Errore nella transizione running-sleaping");
                Thread.currentThread().interrupt();
                return;
            }
        }
        setPunteggio(100);
    }
    

    /**
     * Stampa a video il punteggio finale e la parola scelta dal giocatore.
     */
    public void comunica() {
        System.out.println("Giocatore " + nome + ": " + punteggio + " ha scelto la parola: " + parola);
    }


    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public int getPunteggio() {
        return punteggio;
    }
}
