package utils;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import dao.CarrelloDAO;

import java.sql.SQLException;

@WebListener
public class SessionCleanupListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // viene azionata quando un utente guest, non si logga e scade la sessione
        HttpSession session = se.getSession();
        Integer guestCarrelloId = (Integer) session.getAttribute("carrelloId");
        // recupera l'id del carrelloguest, se esiste lo elimina

        if (guestCarrelloId != null) {
            CarrelloDAO carrelloDAO = new CarrelloDAO();
            try {
                carrelloDAO.eliminaCarrelloGuest(guestCarrelloId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
