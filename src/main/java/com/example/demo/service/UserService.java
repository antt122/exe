package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.enums.Status;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

  final UserRepository userRepository;
  final UserMapper userMapper;

    public User createUser(UserCreationRequest request){

      if (userRepository.existsByUsername(request.getUsername() ))  {
          throw new AppException(ErrorCode.USER_EXISTED);
      }
      User user = userMapper.toUser(request);
      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
      user.setPassword(passwordEncoder.encode(request.getPassword()));

//      Status status = Status.ACTIVE;
//        user.setStatus(status);

      HashSet<String> roles = new HashSet<>();
      roles.add(Role.USER.name());
      user.setRoles(roles);
      return userRepository.save(user);
    }

    public List<UserResponse> getUsers(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }


    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId)));
    }

    @Transactional
    public UserResponse deleteUser(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setStatus(Status.BANNED);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }


}
