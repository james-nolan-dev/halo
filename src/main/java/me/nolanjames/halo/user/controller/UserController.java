package me.nolanjames.halo.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.nolanjames.halo.user.model.UpdateUserRequest;
import me.nolanjames.halo.user.model.UserRequest;
import me.nolanjames.halo.user.model.UserResponse;
import me.nolanjames.halo.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        return new ResponseEntity<>(userService.listAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable int id, @RequestBody @Valid UpdateUserRequest request) {
        return new ResponseEntity<>(userService.updateUser(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.NO_CONTENT);

    }
}
