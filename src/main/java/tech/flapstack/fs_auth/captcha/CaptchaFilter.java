package tech.flapstack.fs_auth.captcha;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import tech.flapstack.fs_auth.AppSettings;

@Captcha
@Provider
public class CaptchaFilter implements ContainerRequestFilter{
    
    @Inject private AppSettings settings;
    @Inject private HttpServletRequest req;
    
    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        if(!isValid(settings.get("captcha.api.secret"),req.getHeader("captchatoken"))){
            crc.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        } 
    }
    
    public boolean isValid(String secret, String token){
        
        if(token == null || ("").equals(token) || secret == null || ("").equals(secret)){
            return false;
        }
        boolean result = false;
        
        Map<String, String> parameters = new HashMap<>();
        parameters.put("secret", secret);
        parameters.put("response", token);
        
        try {
            URL url = new URL(settings.get("captcha.api.url"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
            con.setRequestProperty("charset", "utf-8");
            
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(getParamsString(parameters));
            out.flush();
            out.close();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            
            in.close();
            con.disconnect();
            
            JsonObject response = parseResponse(content.toString());
            result = response.getBoolean("success");   
        } catch (MalformedURLException ex) {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private String getParamsString(Map<String, String> params) 
      throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
 
        for (Map.Entry<String, String> entry : params.entrySet()) {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }
 
        String resultString = result.toString();
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }
    
    private JsonObject parseResponse(String jsonString){
        JsonObject object;
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            object = jsonReader.readObject();
        }
        return object;
    }
}
