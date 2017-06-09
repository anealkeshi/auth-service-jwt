package com.anilkc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "roles")
public class Role implements GrantedAuthority, Serializable {

  private static final long serialVersionUID = -1562723181766676778L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Integer id;

  private String role;

  @JsonIgnore
  @ManyToMany(mappedBy = "roles")
  private List<User> users = new ArrayList<>();

  public Role() {

  }

  public Role(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public void addUser(User user) {
    if (!this.users.contains(user)) {
      this.users.add(user);
    }

    if (!user.getRoles().contains(this)) {
      user.getRoles().add(this);
    }
  }

  public void removeUser(User user) {
    this.users.remove(user);
    user.getRoles().remove(this);
  }

  @Override
  public String getAuthority() {
    return this.role;
  }

}
