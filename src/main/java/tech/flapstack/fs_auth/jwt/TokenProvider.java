package tech.flapstack.fs_auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import tech.flapstack.fs_auth.AppSettings;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TokenProvider {
    
    AppSettings settings;

    public TokenProvider() {
    }
    
    @Inject
    public TokenProvider(AppSettings settings) {
        this.settings = settings;
    }
    
    public String createAccessToken(String subject) throws TokenProviderException{
        return JWT.create()
            .withIssuer("flapstack_auth")
            .withSubject(subject)
            .withExpiresAt(new Date(new Date().getTime() + 3600000))
            .sign(Algorithm.RSA256(null, getKey()));
    }
    
    public String createRenewToken(String subject, UUID uid) throws TokenProviderException{
        return JWT.create()
            .withIssuer("flapstack_auth")
            .withSubject(subject + ":" + uid.toString())
            .sign(Algorithm.RSA256(null, getKey()));
    }

    private RSAPrivateKey getKey() throws TokenProviderException {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("/home/osboxes/keytore.jks"), ("flapstack").toCharArray());
            RSAPrivateKey pk = (RSAPrivateKey) ks.getKey("jwt", ("flapstack").toCharArray());
           
            return pk;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException ex) {
            Logger.getLogger(TokenProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new TokenProviderException("cannot get key for signing");
        }
    }
    
}
