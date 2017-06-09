package com.anilkc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.anilkc.domain.Role;

@Repository("roleRepository")
public interface RoleRepository extends CrudRepository<Role, Integer>{
}