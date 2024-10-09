package com.Job_Prescreening_system.demo.REST;

import com.Job_Prescreening_system.demo.Entities.role;
import com.Job_Prescreening_system.demo.Repositories.RoleRepository;
import com.Job_Prescreening_system.demo.Repositories.UserRepository;
import com.Job_Prescreening_system.demo.Services.UserDetailsImpl;
import com.Job_Prescreening_system.demo.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Job_Prescreening_system.demo.Entities.User;


import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    //OK
    @Secured("ROLE_ADMIN")
    @GetMapping("")
    public List<User> showUsers() {
        return userService.getUsers();
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/admin/{user_id}")
    public List<User> deleteUser(@PathVariable Long user_id) {
        userService.deleteUser(user_id);
        return userService.getUsers();
    }


    //OK
    @Secured("ROLE_ADMIN")
    @PutMapping("{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }






    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/role/delete/{user_id}/{role_id}")
    public ResponseEntity<?> deleteRoleFromUser(@PathVariable Long user_id, @PathVariable Integer role_id) {
        User user = userService.getUser(user_id);
        role role = roleRepository.findById(role_id).orElse(null);

        if (user == null || role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Role not found");
        }

        user.getRoles().remove(role);
        userService.updateUser(user_id, user);

        return ResponseEntity.ok(user);  // Return the updated user as JSON
    }

    //ok
    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/role/add/{user_id}/{role_id}")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long user_id, @PathVariable Integer role_id) {
        User user = userService.getUser(user_id);
        role role = roleRepository.findById(role_id).orElse(null);

        if (user == null || role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Role not found");
        }

        user.getRoles().add(role);
        userService.updateUser(user_id, user);

        return ResponseEntity.ok(user);  // Return the updated user as JSON
    }






    ////11.1


    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<Integer>> getUserRoleIds(@PathVariable Long userId) {
        Set<Integer> roleIds = userService.getRoleIdsByUserId(userId);
        return ResponseEntity.ok(roleIds);
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Integer>> getUserRoleIds(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        Set<Integer> roleIds = userService.getRoleIdsByUserId(userId);
        return ResponseEntity.ok(roleIds);
    }
}








