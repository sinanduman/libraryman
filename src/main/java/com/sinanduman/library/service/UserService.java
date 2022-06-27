package com.sinanduman.library.service;

import com.sinanduman.library.model.Role;
import com.sinanduman.library.model.User;
import com.sinanduman.library.repository.UserRepository;
import com.sinanduman.library.utility.RoleUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {

    Map<String, User> users = new ConcurrentHashMap<>();

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void init() {
        User userOne = User.builder()
                .userId(1)
                .username("sinanduman@gmail.com")
                .password("12344321")
                .role(
                        Role.LIB_BO_C.getRoleName() + "," +
                                Role.LIB_BO_R.getRoleName() + "," +
                                Role.LIB_BO_R_ALL.getRoleName() + "," +
                                Role.LIB_BO_RETURN.getRoleName() + "," +
                                Role.LIB_BM_R.getRoleName() + "," +
                                Role.LIB_BM_C.getRoleName() + "," +
                                Role.LIB_BM_D.getRoleName() + "," +
                                Role.LIB_M_C.getRoleName() + "," +
                                Role.LIB_M_R.getRoleName() + "," +
                                Role.LIB_M_D.getRoleName() + "," +
                                Role.LIB_M_R_ALL.getRoleName()
                )
                .build();

        users.put("sinanduman@gmail.com", userOne);
        userRepository.save(userOne);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if (user != null) {
            Collection<Role> roleList = RoleUtility.findRoles(user.getRole());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), RoleUtility.getAuthoritiesFromRoles(roleList));
        }
        throw new UsernameNotFoundException(username);
    }

    public String getUserIdFromUserName(String username) {
        return String.valueOf(users.get(username).getUserId());
    }
}
