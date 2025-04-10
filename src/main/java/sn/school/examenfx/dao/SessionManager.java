package sn.school.examenfx.dao;

import lombok.Getter;
import lombok.Setter;
import sn.school.examenfx.entities.User;

@Setter
@Getter
public class SessionManager {
    private static SessionManager instance;
    private User CurrentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void logout() {
        this.CurrentUser = null;
    }
}
