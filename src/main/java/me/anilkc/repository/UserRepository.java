package me.anilkc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.anilkc.domain.User;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {

  User findByUsername(String username);
}
