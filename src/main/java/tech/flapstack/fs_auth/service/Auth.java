package tech.flapstack.fs_auth.service;

import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import tech.flapstack.fs_auth.AppSettings;
import tech.flapstack.fs_auth.LoggedInUserStore;
import tech.flapstack.fs_auth.captcha.Captcha;
import tech.flapstack.fs_auth.dao.UserDAO;
import tech.flapstack.fs_auth.entity.FlapstackUser;
import tech.flapstack.fs_auth.jwt.TokenProvider;
import tech.flapstack.fs_auth.jwt.TokenProviderException;

@Path("auth")
public class Auth {
    
    UserDAO userDao;
    AppSettings settings;
    LoggedInUserStore userStore;
    TokenProvider tokenProvider; 
    
    @Inject 
    public Auth(UserDAO userDao, AppSettings settings, LoggedInUserStore userStore, TokenProvider tokenProvider ){
        this.userDao = userDao;
        this.settings = settings;
        this.userStore = userStore;
        this.tokenProvider = tokenProvider;
    }
    
    @POST
    @Path("login")
    @Captcha
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("username") String username, @FormParam("password") String password){
        
        List<FlapstackUser> u = userDao.get(username,true);
        //if(u.size() == 1 && BCrypt.checkpw(u.get(0).getPassword(), password)){
            
            try {
                UUID uid = UUID.randomUUID();
                String accessToken  = tokenProvider.createAccessToken(username);
                String refreshToken  = tokenProvider.createRenewToken(username, uid);
                userStore.getLoggedinUsers().put(username, uid.toString());
                String responseBody = "{\"at\":\""+ accessToken + "\", \"rt\" : \""+ refreshToken +"\" }";
                return Response.ok(responseBody).build();
            } catch (TokenProviderException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        /*} else {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" : \"invalid username or password\"}").build();
        }*/
    }
    
    @GET
    @Path("logout")
    public Response logout(){
        return Response.ok().build();
    }
}
