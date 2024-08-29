package TTT.security;

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserSecurityService implements UserDetailsService {

    private final CustomUserDAO dao = new CustomUserDAO();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = dao.findCustomUser(username);

        return User.withUsername(customUser.getEmail())
                .password(customUser.getPassword())
                .authorities("USER").build();
    }
}
