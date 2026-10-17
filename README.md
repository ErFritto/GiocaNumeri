# GiocaNumeri

Un piccolo progetto Java per vedere come lavorano due thread contemporaneamente.
I giocatori si chiamano **gigi** e **guizo**: ognuno stampa i numeri da 0 a 18,
poi comunica il proprio punteggio e la parola scelta.

## Come funziona

Il programma avvia prima gigi e, dopo 5 secondi, guizo. Tra un numero e il
successivo ogni giocatore aspetta 2 secondi. Per questo le loro stampe si
mescolano: l'ordine può cambiare a ogni esecuzione. Quando entrambi hanno
finito, il programma stampa `Fine gioco!`.

Nella versione principale, `Giocatore` **implementa `Runnable`**. Il metodo
`run()` descrive cosa deve fare un giocatore; la classe `GiocaNumeri` crea un
`Thread` per ciascun giocatore e lo avvia. Le cartelle `src/giocanumeri` e
`src/giocanumeri2` conservano le versioni precedenti, dove `Giocatore`
estendeva `Thread`.

## Informazioni mostrate

All'avvio di ogni giocatore compaiono l'**id**, il **nome**, la **priorità** e
lo **stato** del suo thread. Alla fine compaiono il tempo impiegato da ciascun
giocatore e il tempo totale del gioco. I tempi sono in millisecondi e possono
variare leggermente da una prova all'altra.

I metodi di `Thread` più importanti in questo progetto sono:

| Metodo | A cosa serve |
| --- | --- |
| `start()` | Avvia il thread e fa eseguire `run()`. |
| `sleep()` | Mette in pausa il thread corrente. |
| `yield()` | Suggerisce di lasciare spazio agli altri thread. |
| `join()` | Aspetta che un thread finisca. |
| `currentThread()` | Trova il thread che sta eseguendo il codice. |
| `getId()`, `getName()`, `getPriority()`, `getState()` | Leggono le caratteristiche mostrate all'avvio. |

Per misurare le durate viene usato `System.nanoTime()`: non è un metodo di
`Thread`. Se una pausa o un'attesa viene interrotta, il programma gestisce
l'eccezione `InterruptedException`.

## Come avviarlo

Apri il progetto in IntelliJ IDEA ed esegui `GiocaNumeri.java` nella cartella
`src`. Puoi anche usare il terminale dalla cartella principale del progetto:

```bash
javac -encoding utf-8 -d out src/Giocatore.java src/GiocaNumeri.java
java -cp out GiocaNumeri
```

Il progetto è disponibile anche su [GitHub](https://github.com/ErFritto/GiocaNumeri).
