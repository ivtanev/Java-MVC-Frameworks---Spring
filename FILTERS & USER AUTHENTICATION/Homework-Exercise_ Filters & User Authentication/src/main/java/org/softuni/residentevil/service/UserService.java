package org.softuni.residentevil.service;

import org.softuni.residentevil.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean registerUser(UserServiceModel userServiceModel);
}
