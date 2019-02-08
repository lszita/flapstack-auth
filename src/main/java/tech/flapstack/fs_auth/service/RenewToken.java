package tech.flapstack.fs_auth.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("renew")
public class RenewToken {
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response renew(){
        return null;
    }
}
