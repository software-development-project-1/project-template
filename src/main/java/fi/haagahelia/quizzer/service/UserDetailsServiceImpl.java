package fi.haagahelia.quizzer.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import fi.haagahelia.quizzer.repository.AppUserRepository;
import fi.haagahelia.quizzer.model.AppUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser newUser = appUserRepository.findByUserName(username);

        if(newUser == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new User(newUser.getUserName(), newUser.getPassword(), new ArrayList<>());
    }
}       