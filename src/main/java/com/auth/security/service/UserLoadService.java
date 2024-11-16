package com.auth.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLoadService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findTopByUsername(username)
                .map(UserContainer::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

    }

    public UserContainer loadUserById(String userId) throws UsernameNotFoundException {
        return userRepository.findById(userId)
                .map(UserContainer::new)
                .orElseThrow(() -> new UsernameNotFoundException("user with id " + userId + " not found "));

    }
}
