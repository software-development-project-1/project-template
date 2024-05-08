package fi.haagahelia.quizzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import fi.haagahelia.quizzer.repository.AppUserRepository;
import fi.haagahelia.quizzer.model.AppUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.repository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser currUser = repository.findByUserName(username);
        UserDetails user = new org.springframework.security.core.userdetails.User(username, currUser.getPassword(), AuthorityUtils.createAuthorityList(currUser.getRole()));
        return user;
    }
}       