# GiocaNumeri — versione con Runnable

La versione principale si trova in `src/Giocatore.java` e `src/GiocaNumeri.java`.
`Giocatore` implementa `Runnable`: il suo metodo `run()` contiene il lavoro da
eseguire, mentre `GiocaNumeri` crea un `Thread` per ciascun giocatore e chiama
`start()`. Le cartelle `src/giocanumeri` e `src/giocanumeri2` contengono le
precedenti versioni in cui `Giocatore extends Thread`.

## 1. Comportamento del programma

Il programma simula una partita con due giocatori concorrenti:

1. `GiocaNumeri.main` stampa `Benvenuto`, crea due oggetti `Giocatore`
   (`gigi` e `guizo`), li passa a due oggetti `Thread` e li avvia con `start()`.
2. Tra i due `start()` il thread main si sospende per 5 secondi con
   `Thread.sleep(5000)`, quindi `guizo` parte in ritardo rispetto a `gigi`.
3. Ogni thread esegue in parallelo `Giocatore.run()` → `gioca()` + `comunica()`:
   all'avvio mostra id, nome, priorità e stato del thread; alla fine mostra il
   tempo impiegato dal giocatore.
   - `gioca()` stampa i numeri da 0 a 18, cedendo ogni volta il turno con
     `Thread.yield()` e dormendo 2 secondi con `sleep(2000)`;
      sceglie `parola = "Buongiorno"` prima del ciclo e alla fine imposta
      `punteggio = 100`.
   - `comunica()` stampa punteggio finale e parola scelta.
4. Il main attende la fine di entrambi con `t.join()` e `t2.join()`,
   poi stampa il tempo totale e `Fine gioco!`.

Poiché i thread condividono lo standard output, l'ordine delle stampe
è non deterministico (concorrenza).

## 2. Metodi della classe `Thread` usati

| Metodo | Dove | Effetto |
|---|---|---|
| `start()` | `GiocaNumeri.java` (su `t`, `t2`) | Crea un nuovo flusso di esecuzione che chiama `run()` sul `Runnable` passato al costruttore. Ritorna subito: il chiamante prosegue in parallelo. Una chiamata diretta a `run()` non creerebbe un thread. |
| `run()` | `Giocatore.java` (implementazione di `Runnable`) | Definisce il compito del giocatore: `gioca()` poi `comunica()`. |
| `static sleep(millis)` | `GiocaNumeri.java` (`sleep(5000)`), `Giocatore.java` (`sleep(2000)`) | Sospende il thread corrente (timed-waiting) per i millisecondi indicati. Non rilascia lock; al risveglio torna runnable. |
| `join()` | `GiocaNumeri.java` (`t.join()`, `t2.join()`) | Il main si blocca finché il thread atteso non termina; se è già terminato ritorna subito. Serve a sincronizzare la fine ("Fine gioco!" solo dopo entrambi). |
| `static yield()` | `Giocatore.java` (`Thread.yield()` nel ciclo) | Suggerimento allo scheduler: il thread corrente cede la CPU e torna runnable, favorendo l'alternanza con l'altro giocatore. È solo un hint, senza garanzie. |
| `static currentThread()` | `Giocatore.java` | Restituisce il thread che sta eseguendo `run()`. |
| `getId()`, `getName()`, `getPriority()`, `getState()` | `Giocatore.java` | Restituiscono id, nome, priorità e stato del thread. Lo stato osservato dal thread stesso durante `run()` è normalmente `RUNNABLE`; non descrive tutti gli stati attraversati in seguito. |

## 3. Tempi di esecuzione

`System.nanoTime()` misura il tempo trascorso: per ciascun giocatore dall'inizio
alla fine di `run()`, e per il gioco dall'avvio del primo thread al termine di
entrambi i `join()`. I risultati sono mostrati in millisecondi. `Thread` non
fornisce un metodo per misurare direttamente questa durata; `sleep()` introduce
invece le pause previste dal gioco. I tempi comprendono attese e scheduling,
quindi possono variare tra esecuzioni.

## 4. Eccezioni usate

### `InterruptedException` (checked)
- Lanciata da `sleep()` e `join()` quando il thread in attesa viene interrotto.
- Essendo checked, il compilatore obbliga a gestirla o dichiararla:
  - in `Giocatore.gioca()`: blocco `try/catch` attorno a `sleep(2000)`;
    in caso di interruzione stampa l'errore, ripristina il flag con
    `Thread.currentThread().interrupt()` ed esce con `return`;
  - in `GiocaNumeri.main`: blocchi `try/catch` attorno a `sleep(5000)` e ai due
    `join()`; in caso di interruzione viene stampato un messaggio.

Non vengono usate eccezioni unchecked particolari; eventuali
`RuntimeException` nei `run()` terminerebbero il singolo thread e verrebbero
segnalate dal gestore delle eccezioni non catturate.

## 5. Compilazione ed esecuzione

```bash
javac -encoding utf-8 src/Giocatore.java src/GiocaNumeri.java -d out
java -cp out GiocaNumeri
```

## 6. Repository

Il progetto è collegato al repository GitHub
[`ErFritto/GiocaNumeri`](https://github.com/ErFritto/GiocaNumeri).
Per sincronizzare le modifiche locali, dopo averle controllate:

```bash
git add src/Giocatore.java src/GiocaNumeri.java README.md
git commit -m "Aggiunge informazioni sui thread e tempi di esecuzione"
git push origin main
```
