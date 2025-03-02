package com.optimagrowth.license.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.license.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>  {
    User findByEmail(String email);
    User findByUsername(String username);
}
