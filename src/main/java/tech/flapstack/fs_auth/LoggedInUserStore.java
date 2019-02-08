package tech.flapstack.fs_auth;

import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class LoggedInUserStore {
    
    private ConcurrentHashMap<String, String> loggedinUsers;

    @PostConstruct
    public void init() {
        loggedinUsers = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, String> getLoggedinUsers() {
        return loggedinUsers;
    }
}
