package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;


import com.optimagrowth.license.model.User;
import com.optimagrowth.license.service.UserService;

@RestController
@RequestMapping(value="v1/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value="/{userId}",method = RequestMethod.GET)
	public ResponseEntity<User> getUser( @PathVariable("userId") Long userId) {
		User user = userService.getUser(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
		user.setPassword(null); // set password to null
        return ResponseEntity.ok(user); // Return user with 200 OK status
	}

	@PutMapping("/{userId}")
	public ResponseEntity<User> update(@PathVariable Long userId, @RequestBody User user){
		User newuser = userService.updateUser(userId, user);
        if (newuser == null) {
            return ResponseEntity.notFound().build();
        }
        newuser.setPassword(null); // set password to null
        return ResponseEntity.ok(newuser); // Return user with 200 OK status
	}
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@DeleteMapping(value="/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        HttpStatus status = userService.deleteUser(userId); // Call the service

        // Return appropriate ResponseEntity based on the status from the service
        if (status == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("User not found");
        } else if (status == HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while deleting the user");
        }
	}

    // login user endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.authenticateAndGenerateToken(username, password);
        if (token != null) {
            return ResponseEntity.ok(token); // Return JWT token
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Invalid credentials");
        }
    }
}

