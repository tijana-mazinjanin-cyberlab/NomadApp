package Services;

import Repositories.ConfirmationTokenRepository;
import Repositories.IRepository;
import model.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class ConfirmationTokenService {
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken findByConfirmationToken(String confirmationToken){
        return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    }
    public void create(ConfirmationToken confirmationToken){
        confirmationTokenRepository.save(confirmationToken);
    }
}
