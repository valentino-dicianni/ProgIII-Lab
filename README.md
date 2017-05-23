# Progammazione III-Lab
Progetto di Programmazione III - Dipartimento di Informatica UNITO (Torino, Italy)
Anno 2016/2017

# Comopnenti del Gruppo:
  - Valentino Di Cianni
  - Daniele Accossato
  - Francesco Pilittu
  
# Specifiche del Progetto:
Progetto di laboratorio – Programmazione III - 2016/2016

Si sviluppi un’applicazione java che implementi un servizio di posta elettronica
organizzato con un mail server che gestisce le caselle di posta elettronica degli utenti
e i mail client necessari per permettere agli utenti di accedere alle proprie caselle di
posta. Si assuma di avere 3 utenti di posta elettronica che comunicano tra loro.
  - Il mail server gestisce una lista di caselle di posta elettronica. Il mail server ha
  un’interfaccia grafica sulla quale viene visualizzato il log delle azioni effettuate
  dai mail clients e degli eventi che occorrono durante l’interazione tra i client e il
  server. Per esempio: apertura/chiusura di una connessione tra mail client e server,
  invio di messaggi da parte di un client, ricezione di messaggi da parte di un client,
  errori nella consegna di messaggi, eliminazione di messaggi, etc. (tutte le tipologie
  di azioni permesse dai client – NON fare log di eventi locali al client come per
  esempio il fatto che ha schiacciato un bottone, aperto una finestra o simili in
  quanto non sono di pertinenza del server.
  - Una casella di posta elettronica contiene:
    - Nome dell’account di mail associato alla casella postale (es.
    giorgio@mia.mail.com).
    - Lista eventualmente vuota di messaggi. I messaggi di posta elettronica
    sono istanze di una classe Email che specifica ID, mittente, destinatario,
    argomento, testo, priorità e data di spedizione del messaggio.
    
    
- Il mail client, associato ad un particolare account di posta elettronica, ha
un’interfaccia grafica così caratterizzata:
  - L’interfaccia permette di:
    - creare e inviare un messaggio a uno o più destinatari
    - leggere i messaggi della casella di posta
    - rispondere a un messaggio ricevuto, in Reply (al mittente del
    destinatario) e/o in Reply-all (al mittente e a tutti i destinatari del
    messaggio ricevuto)
    - girare (forward) un messaggio a uno o più account di posta
    elettronica
    - rimuovere un messaggio dalla casella di posta.
  - L’interfaccia mostra sempre la lista aggiornata dei messaggi in casella e,
  quando arriva un nuovo messaggio, notifica l’utente attraverso una finestra
  di dialogo che mostra mittente e il titolo del messaggio.
  - NB: per semplicità si associno i mail client agli utenti a priori: non si
  richiede che il mail client offra le funzionalità di registrazione di un
  account di posta. Inoltre, un mail client è associato ad una sola casella di
  posta elettronica e la sua interfaccia non richiede autenticazione da parte
  dell’utente.
  
# Requisiti tecnici:
- L’applicazione deve essere sviuppata in Java e basata su architettura MVC, con
Controller + viste e Model, seguendo il pattern Observer Observable. Si noti che
non deve esserci comunicazione diretta tra viste e model: ogni tipo di
comunicazione tra questi due livelli deve essere mediato dal controller o
supportata dal pattern Observer Observable.•

- L’applicazione deve permettere all’utente di correggere eventuali input errati (per
es., in caso di inserimento di indirizzi di posta elettronica non esistenti, il server
deve inviare messaggio di errore al client che ha inviato il messaggio).
- L’applicazione deve parallelizzare le attività che non necessitano di esecuzione
sequenziale e gestire gli eventuali problemi di accesso a risorse in mutua
esclusione. In particolare, i client e il server di mail devono essere thread distinti e
la creazione/gestione dei messaggi deve avvenire in parallelo alla ricezione di altri
messaggi.
- L’applicazione deve essere distribuita (i mail client e il server devono stare tutti
su JVM distinte) attraverso l’uso di RMI.
# Requisiti dell’interfaccia utente:
- L’interfaccia utente deve essere:
  - Comprensibile (trasparenza). In particolare, a fronte di errori, deve
  segnalare il problema all’utente.
  - Ragionevolmente efficiente per permettere all’utente di eseguire le
  operazioni con un numero minimo di click e di inserimenti di dati.
  - Deve essere implementata utilizzando il linguaggio Java e in particolare
  SWING e Thread java.
  
# Note:
- Si raccomanda di prestare molta attenzione alla progettazione dell’applicazione
per facilitare il parallelismo nell’esecuzione delle istruzioni e la distribuzione su
JVM diverse.
- Si ricorda che il progetto può essere svolto in gruppo (max 3 persone) o
individualmente. Se lo si svolge in gruppo la discussione deve essere fatta
dall’intero gruppo in soluzione unica. La discussione potrà essere fatta nelle date
di appello orale del corso oppure su appuntamento, concordando la data con il
docente via email. Si può discutere il progetto prima o dopo aver sostenuto la
prova scritta. Il voto finale deve essere registrato entro fine febbraio 2018, data
oltre la quale non è possibile mantere i voti parziali. Leggere il regolamento
d’esame sulla pagina web del corso per dettagli.
