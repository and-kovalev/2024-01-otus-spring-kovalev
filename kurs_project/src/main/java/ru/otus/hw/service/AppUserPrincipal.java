package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.otus.hw.dto.UserDto;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class AppUserPrincipal implements UserDetails {

    private final UserDto userDto;

    private final BCryptPasswordEncoder cryptPasswordEncoder;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.checkedList(Stream.of(userDto.getUserType().getRole().split(",", -1))
                        .map(role -> (GrantedAuthority) () -> role)
                        .collect(Collectors.toList()),
                GrantedAuthority.class);
    }

    @Override
    public String getPassword() {
        return cryptPasswordEncoder.encode(userDto.getPass());
    }

    @Override
    public String getUsername() {
        return userDto.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
