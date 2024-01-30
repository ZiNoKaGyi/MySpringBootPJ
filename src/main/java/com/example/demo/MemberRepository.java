package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Integer>{

	public  Member findByUsername(String username);

	public Member getReferenceById(MemberDetails loggedInMember);

	public void save(OrderItem orderItem);

	

	

}
