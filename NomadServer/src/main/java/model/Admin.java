package model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import model.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Entity
@DiscriminatorValue("admin")
public class Admin extends AppUser{
    @Override
    public Collection<UserType> getAuthorities() {
        return Collections.singletonList(UserType.ADMIN);
    }
    public Admin(){
        super();
    };
}
