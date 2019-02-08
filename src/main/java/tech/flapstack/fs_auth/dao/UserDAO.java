package tech.flapstack.fs_auth.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import tech.flapstack.fs_auth.entity.FlapstackUser;

@Stateless
public class UserDAO {
    
    @PersistenceContext(unitName = "authPU")
    private EntityManager em;

    public UserDAO() {
    }

    public UserDAO(EntityManager em) {
        this.em = em;
    }

    public List<FlapstackUser> get(String username){
        Query q = em.createQuery("SELECT u FROM FlapstackUser u WHERE u.name = :username");
        q.setParameter("username", username);
        return q.getResultList();
    }
    
    public List<FlapstackUser> get(String username, String email){
        Query q = em.createQuery("SELECT u FROM FlapstackUser u WHERE u.name = :username OR u.email = :email");
        q.setParameter("username", username);
        q.setParameter("email", email);
        return q.getResultList();
    }
    
    public List<FlapstackUser> get(String username, boolean isActive){
        Query q = em.createQuery("SELECT u FROM FlapstackUser u WHERE u.name = :username AND u.isActive = :isActive");
        q.setParameter("username", username);
        q.setParameter("isActive", isActive);
        return q.getResultList();
    }
    
    
    
    public FlapstackUser save(FlapstackUser u){
        em.persist(u);
        return u;
    }
}
