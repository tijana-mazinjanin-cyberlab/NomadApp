package Services;

import Repositories.IRepository;
import Repositories.UserRepository;
import model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class UserService implements IService<AppUser, Long>, UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder pass){
        passwordEncoder = pass;
    }

    public void confirmRegistration(String username){
        AppUser user = (AppUser)this.loadUserByUsername(username);
        user.setVerified(true);
        this.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }
    public boolean isRegistered(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findOneByUsername(username);
        return user != null;
    }

    @Override
    public Collection<AppUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public AppUser findOne(Long id) {
        return userRepository.findOneById(id);
    }

    @Override
    public void create(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        userRepository.save(appUser);
    }
    public void save(AppUser appUser) {
        userRepository.save(appUser);
    }

    @Override
    public void update(AppUser appUser) {
        AppUser existingUser = findOne(appUser.getId());
        if ((existingUser!= null) && (!appUser.getPassword().equals(existingUser.getPassword()))){
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            System.out.println("new password: " + appUser.getPassword());
            appUser.setLastPasswordResetDate(new Timestamp(new Date().getTime()));
        }

        userRepository.save(appUser);
    }

    public void update2(AppUser appUser) {
        userRepository.save(appUser);
    }


    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
