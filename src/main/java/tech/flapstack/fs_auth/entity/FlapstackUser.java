package tech.flapstack.fs_auth.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
public class FlapstackUser implements Serializable {
    
    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    @NotNull(message = "Username cannot be null")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must contain only alphanumeric symbols")
    @Size(min = 3, max = 30, message = "Username must be 3-30 characters")
    private String name;
    @NotNull(message = "Password cannot be null")
    @Size(min = 3, max = 30, message = "Password must be 3-30 characters")
    private String password;
    @NotNull(message="Email address cannot be null")
    @Column(unique = true)
    @Email(message = "Email address must be a valid email address")
    @Size(max = 50, message = "Email address must be 3-30 characters")
    private String email;
    private boolean isActive = false;

    public FlapstackUser() {
    }
    
    @PrePersist
    protected void encodePassword(){
        BCrypt.hashpw(this.password, BCrypt.gensalt());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
