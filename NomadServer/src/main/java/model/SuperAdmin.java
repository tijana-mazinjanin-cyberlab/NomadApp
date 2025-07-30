package model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import model.enums.UserType;

import java.util.Collection;
import java.util.Collections;
@Entity
@DiscriminatorValue("super_admin")
public class SuperAdmin extends AppUser{
    @Override
    public Collection<UserType> getAuthorities() {
        return Collections.singletonList(UserType.SUPER_ADMIN);
    }
    public SuperAdmin(){
        super();
    };
}