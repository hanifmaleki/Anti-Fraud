package antifraud.security;

import antifraud.model.User;
import antifraud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AntifraudUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUser(username);
        if(user==null){
            throw new UsernameNotFoundException("The username "+ username + "could not be found");
        }

        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new RoleAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(), authorities);
    }
}
