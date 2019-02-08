package tech.flapstack.fs_auth.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import tech.flapstack.fs_auth.captcha.Captcha;
import tech.flapstack.fs_auth.dao.UserDAO;
import tech.flapstack.fs_auth.entity.FlapstackUser;

@Path("register")
public class Register {
    
    UserDAO userDao;
    
    @Inject
    public Register(UserDAO userDao) {
        this.userDao = userDao;
    }
    
    @POST
    @Captcha
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Valid FlapstackUser u){
        try{
            userDao.save(u);
            return Response.ok().build();
        } catch (Exception ex){
            List<FlapstackUser> users = userDao.get(u.getName(), u.getEmail());
            
            if(users == null || users.isEmpty()){
                Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
                return Response.serverError().build();
            } else {
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for(FlapstackUser user : users){
                    if(user.getName().equalsIgnoreCase(u.getName()))
                        arrayBuilder.add("Username already exists");
                    if(user.getEmail().equalsIgnoreCase(u.getEmail()))
                        arrayBuilder.add("Email address already exists");
                }
            return Response.status(Response.Status.BAD_REQUEST).entity(arrayBuilder.build()).build();
            }
        }
    }
}
