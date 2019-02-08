package tech.flapstack.fs_auth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class AppSettings {

    private Properties props;
    
    @PostConstruct
    public void readSettings(){
        try (InputStream is = new FileInputStream("app.properties")){
            
            props = new Properties();
            props.load(is);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String get(String key){
        return this.props.getProperty(key);
    }
   
}
