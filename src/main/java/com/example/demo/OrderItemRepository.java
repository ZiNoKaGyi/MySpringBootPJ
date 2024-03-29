package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	


	Member getReferenceById(MemberDetails loggedInMember);

	List<OrderItem> findAllByMemberId(int loggedInMemberId);

}
