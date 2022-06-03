package com.cos.jwt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	private String roles; // user, admin
		
	// role이 한 유저에게 2개 이상 있는 경우 대비 
	public List<String> getRoleList(){
		if (this.roles.length() > 0) {
			return Arrays.asList(this.roles.split("."));
		}
		return new ArrayList<>();
	}
}
