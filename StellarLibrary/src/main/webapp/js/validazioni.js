document.addEventListener("DOMContentLoaded", () => {
    // Login
    const loginForm = document.getElementById("login-form");
    if (loginForm) {
        loginForm.addEventListener("submit", e => {
            const email = loginForm.email.value.trim();
            const password = loginForm.password.value.trim();
            if (!email || !password) {
                alert("Compila tutti i campi.");
                e.preventDefault();
            }
        });
    }

    // Registrazione
    const registerForm = document.getElementById("register-form");
    if (registerForm) {
        registerForm.addEventListener("submit", e => {
            const nome = registerForm.nome.value.trim();
            const cognome = registerForm.cognome.value.trim();
            const email = registerForm.email.value.trim();
            const password = registerForm.password.value.trim();

            if (!nome || !cognome || !email || !password) {
                alert("Compila tutti i campi.");
                e.preventDefault();
            } else if (password.length < 8) {
                alert("La password deve avere almeno 8 caratteri.");
                e.preventDefault();
            }
        });
    }
    document.querySelector(".checkout-form").addEventListener("submit", function (e) {
        const indirizzo = document.querySelector("input[name='idIndirizzo']:checked");
        const metodo = document.getElementById("metodoPagamento");

        if (!indirizzo) {
            e.preventDefault();
            alert("Seleziona un indirizzo di spedizione.");
            return;
        }

        if (!metodo.value) {
            e.preventDefault();
            alert("Seleziona un metodo di pagamento.");
        }
    });
    document.getElementById("formRecensione").addEventListener("submit", function(e) {
        const valutazione = document.getElementById("valutazione").value;
        const titolo = document.getElementById("titolo").value.trim();

        if (!valutazione || valutazione < 1 || valutazione > 5) {
            e.preventDefault();
            alert("Inserisci una valutazione valida tra 1 e 5.");
        }

        if (!titolo) {
            e.preventDefault();
            alert("Inserisci un titolo per la recensione.");
        }
    });
});
