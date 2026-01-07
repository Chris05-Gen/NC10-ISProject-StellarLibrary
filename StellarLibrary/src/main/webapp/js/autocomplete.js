
// ---------------------------------AUTOCOMPLETE ---------------------------------

document.addEventListener("DOMContentLoaded", function () {
    const searchBox = document.getElementById('search-box');
    const listEl = document.getElementById('autocomplete-list');

    searchBox.addEventListener('input', function () {
        const term = this.value.trim();
        if (!term) {
            listEl.innerHTML = '';
            return;
        }

        fetch(`AutocompleteServlet?term=${encodeURIComponent(term)}`)
            .then(res => res.json())
            .then(data => {
                listEl.innerHTML = '';
                data.forEach(libro => {
                    const div = document.createElement('div');
                    div.textContent = libro.titolo;
                    div.dataset.isbn = libro.isbn;
                    div.addEventListener('click', () => {
                        window.location.href = `DettaglioLibroServlet?isbn=${libro.isbn}`;
                    });
                    listEl.appendChild(div);
                });
            })
            .catch(err => console.error("Errore fetch autocomplete:", err));
    });

// Chiude i suggerimenti cliccando fuori
    document.addEventListener('click', function (e) {
        if (!searchBox.contains(e.target)) {
            listEl.innerHTML = '';
        }
    });
});