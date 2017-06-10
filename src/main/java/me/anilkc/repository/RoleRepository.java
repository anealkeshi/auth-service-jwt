package me.anilkc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.anilkc.domain.Role;

@Repository("roleRepository")
public interface RoleRepository extends CrudRepository<Role, Integer>{
}