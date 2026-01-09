# 📚 Stellar Library

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JSP](https://img.shields.io/badge/Jakarta%20EE-000000?style=for-the-badge&logo=eclipse&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black)

**Stellar Library** è un'applicazione web completa per la gestione di una libreria online. Il progetto implementa un'architettura **MVC** (Model-View-Controller) utilizzando **Java Servlets** e **JSP**, offrendo funzionalità sia per gli utenti finali (acquisto libri) che per gli amministratori (gestione ordini e catalogo).

## 🚀 Funzionalità Principali

### 👤 Lato Utente
* **Catalogo Libri:** Navigazione tra i libri disponibili con filtri di ricerca.
* **Carrello:** Gestione dinamica del carrello acquisti.
* **Checkout:** Processo di acquisto semplificato.
* **Recensioni:** Possibilità di lasciare recensioni sui libri acquistati.
* **Account:** Registrazione e login utente.

### 🛡️ Lato Admin
* **Dashboard Ordini:** Visualizzazione completa dello storico ordini con dettagli su clienti, indirizzi e pagamenti.
* **Gestione Catalogo:** Aggiunta di nuovi libri al database con upload copertine.
* **Moderazione Recensioni:** Pannello per visualizzare e rimuovere recensioni utente.
* **Statistiche:** (Se presenti) Monitoraggio delle vendite.

### 🎨 Interfaccia e UX
* **Design Responsive:** Layout ottimizzato per Desktop, Tablet e Mobile.
* **Menu Mobile:** Navigazione fluida con menu "hamburger" animato (no flickering).
* **Card View:** Visualizzazione ottimizzata delle tabelle dati su schermi piccoli.

## 🛠️ Tecnologie Utilizzate

* **Backend:** Java (JDK 17+), Jakarta Servlet API.
* **Frontend:** JSP (JavaServer Pages), JSTL, HTML5, CSS3 Custom, JavaScript Vanilla.
* **Database:** MySQL.
* **Server:** Apache Tomcat 10/11.
* **Librerie Esterne:**
    * SwiperJS (per gli slider).
    * FontAwesome (per le icone).

## ⚙️ Installazione e Setup

1.  **Clona la repository:**
    ```bash
    git clone [https://github.com/Chris05-Gen/NC10-ISProject-StellarLibrary.git](https://github.com/Chris05-Gen/NC10-ISProject-StellarLibrary.git)
    ```

2.  **Configura il Database:**
    * Importa il file `.sql` (se presente nella cartella `/sql` o `/db`) nel tuo server MySQL.
    * Modifica il file `src/main/java/utils/DBManager.java` (o equivalente) con le tue credenziali:
        ```java
        String url = "jdbc:mysql://localhost:3306/stellarlibrary";
        String username = "root";
        String password = "tua_password";
        ```

3.  **Importa nell'IDE:**
    * Apri il progetto con IntelliJ IDEA o Eclipse (Enterprise Edition).
    * Assicurati che le librerie (Tomcat, MySQL Connector) siano nel `classpath` o gestite via Maven/Gradle.

4.  **Deploy:**
    * Configura l'artifact di deploy su **Apache Tomcat**.
    * Avvia il server e visita `http://localhost:8080/StellarLibrary`.

## 📂 Struttura del Progetto

```text
StellarLibrary/
├── src/
│   ├── main/
│   │   ├── java/           # Codice Backend (Controller, Model, DAO, Service)
│   │   └── webapp/         # Frontend (JSP, CSS, JS, Img)
│   │       ├── css/        # Fogli di stile (style.css, ecc.)
│   │       ├── js/         # Script (index.js)
│   │       ├── Interface/  # Pagine JSP (ordiniUtenti.jsp, ecc.)
└── README.md