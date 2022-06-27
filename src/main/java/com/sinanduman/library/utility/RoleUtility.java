package com.sinanduman.library.utility;

import com.sinanduman.library.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class RoleUtility {

    public static Collection<Role> findRoles(String roles) {
        Collection<Role> roleList = new ArrayList<>();
        String[] roleArray = roles.split(",");
        Set<Role> roleSet = Set.of(Role.values());
        for (String str : roleArray) {
            Optional<Role> role = roleSet.stream().filter(r -> r.getRoleName().equals(str)).findFirst();
            role.ifPresent(roleList::add);
        }
        return roleList;
    }

    public static Collection<Role> findRolesFromList(Collection<String> roles) {
        Collection<Role> roleList = new ArrayList<>();
        Set<Role> roleSet = Set.of(Role.values());
        for (String str : roles) {
            Optional<Role> role = roleSet.stream().filter(r -> r.getRoleName().equals(str)).findFirst();
            role.ifPresent(roleList::add);
        }
        return roleList;
    }

    public static Collection<? extends GrantedAuthority> getAuthoritiesFromRoles(Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

    public static Collection<? extends GrantedAuthority> getAuthoritiesFromStringList(Collection<String> roles) {
        return getAuthoritiesFromRoles(findRolesFromList(roles));
    }
}
