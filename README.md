# GiocaNumeri — Analisi del progetto

## 1. Comportamento del programma

Il programma simula una partita con due giocatori concorrenti:

1. `GiocaNumeri.main` stampa `Benvenuto`, crea due oggetti `Giocatore`
   (`gigi` e `guizo`) e li avvia come thread con `start()`.
2. Tra i due `start()` il thread main si sospende per 5 secondi con
   `Thread.sleep(5000)`, quindi `guizo` parte in ritardo rispetto a `gigi`.
3. Ogni `Giocatore` esegue in parallelo `run()` → `gioca()` + `comunica()`:
   - `gioca()` stampa i numeri da 0 a 18, cedendo ogni volta il turno con
     `Thread.yield()` e dormendo 2 secondi con `sleep(2000)`;
     alla fine imposta `parola = "Buongiorno"` e `punteggio = 100`.
   - `comunica()` stampa punteggio finale e parola scelta.
4. Il main attende la fine di entrambi con `g1.join()` e `g2.join()`,
   poi stampa `Fine gioco!`.

Poiché i thread condividono lo standard output, l'ordine delle stampe
è non deterministico (concorrenza).

## 2. Metodi della classe `Thread` usati

| Metodo | Dove | Effetto |
|---|---|---|
| `start()` | `GiocaNumeri.java` (su `g1`, `g2`) | Crea un nuovo flusso di esecuzione che chiama `run()`. Ritorna subito: il chiamante prosegue in parallelo. Diverso da `run()` diretto, che non creerebbe alcun thread. |
| `run()` | `Giocatore.java` (override) | Definisce il compito del thread: `gioca()` poi `comunica()`. Non va chiamato direttamente per avviare il thread. |
| `static sleep(millis)` | `GiocaNumeri.java` (`sleep(5000)`), `Giocatore.java` (`sleep(2000)`) | Sospende il thread corrente (timed-waiting) per i millisecondi indicati. Non rilascia lock; al risveglio torna runnable. |
| `join()` | `GiocaNumeri.java` (`g1.join()`, `g2.join()`) | Il main si blocca finché il thread atteso non termina; se è già terminato ritorna subito. Serve a sincronizzare la fine ("Fine gioco!" solo dopo entrambi). |
| `static yield()` | `Giocatore.java` (`Thread.yield()` nel ciclo) | Suggerimento allo scheduler: il thread corrente cede la CPU e torna runnable, favorendo l'alternanza con l'altro giocatore. È solo un hint, senza garanzie. |

## 3. Eccezioni usate

### `InterruptedException` (checked)
- Lanciata da `sleep()` e `join()` quando il thread in attesa viene interrotto.
- Essendo checked, il compilatore obbliga a gestirla o dichiararla:
  - in `Giocatore.gioca()`: blocco `try/catch` attorno a `sleep(2000)`;
    in caso di interruzione stampa l'errore, ripristina il flag con
    `Thread.currentThread().interrupt()` ed esce con `return`;
  - in `GiocaNumeri.main`: blocchi `try/catch` attorno a `sleep(5000)` e ai due
    `join()`; in caso di interruzione viene stampato un messaggio.

Non vengono usate eccezioni unchecked particolari; eventuali
`RuntimeException` nei `run()` causerebbero la terminazione silenziosa
del singolo thread (comportamento standard dei thread).

## 4. Compilazione ed esecuzione

```bash
javac -encoding utf-8 src/Giocatore.java src/GiocaNumeri.java -d out
java -cp out GiocaNumeri
```
