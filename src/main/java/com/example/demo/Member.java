package com.example.demo;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Member {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@NotNull
	@NotEmpty(message="Name cannot be empty!")
	@Size(min=5 ,max=50, message="Name length must be 5-50")
	private String name;
	@NotNull
	@NotEmpty(message="Username cannot be empty!")
	@Size(min=5 ,max=50, message="Username length must be 5-50")
	private String username;
	@NotNull
	@NotEmpty(message="Password cannot be empty!")
	@Size(min=8, message="Password length must be at least 8 digits!")
	private String password;
	@NotNull
	@NotEmpty(message="Email cannot be empty!")
	private String email;
	private String role;
	
	@OneToMany(mappedBy="member", cascade=CascadeType.ALL)
	private Set<CartItem> items;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
