package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.User;
import com.Job_Prescreening_system.demo.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {





    @Autowired
    private UserRepository userRepository;
    @Transactional
    public User updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setEmail(user.getEmail());
            // ... other properties

            userRepository.save(existingUser);
        }
        return existingUser;
    }

    @Transactional
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User saveUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Transactional
    public  void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public  User getUser(Long userId) {
        return userRepository.findById(userId).get();
    }






    @Transactional
    public Set<Integer> getRoleIdsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getRoles().stream().map(role -> role.getId()).collect(Collectors.toSet());
        } else {
            return Collections.emptySet(); // or throw an exception, depending on your use case
        }
    }

}
