package be.atheneumboom.bibliotheek.model.token;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
     private final ConfirmationTokenRepo confirmationTokenRepo;

     public void saveConfirmationToken(ConfirmationToken confirmationToken){
         confirmationTokenRepo.save(confirmationToken);
     }

    public Optional<ConfirmationToken> getToken(String token) {
         return confirmationTokenRepo.findByToken(token);
    }

    public void setConfirmedAt(String token) {
         ConfirmationToken CFtoken = confirmationTokenRepo.findByToken(token).orElseThrow(()->new UsernameNotFoundException("token not found"));
         CFtoken.setConfirmedAt(LocalDateTime.now());
         confirmationTokenRepo.save(CFtoken);
    }

    public boolean deleteToken(String token) {
          return confirmationTokenRepo.findByToken(token)
                    .map(confirmationToken -> {
                         confirmationTokenRepo.delete(confirmationToken);
                         return true;
                    })
                    .orElse(false);
    }


    public List<ConfirmationToken> list() {
         return confirmationTokenRepo.findAll();
    }
}
