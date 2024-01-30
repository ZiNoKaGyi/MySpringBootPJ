package com.example.demo;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HistoryController {

	@Autowired
	private OrderItemRepository orderItemRepository;
	

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/history")
	public String viewFeedback(Model model, Principal principal) {
		if (principal != null) {
			MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Member member = loggedInMember.getMember();
			model.addAttribute("member", member);

		}

		List<OrderItem> orderItemList = orderItemRepository.findAll();
		model.addAttribute("orderItemList", orderItemList);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList); 

		return "view_history";
	}

	@GetMapping("/historyuser")
	public String viewHistoryByMember(Model model, Principal principal) {
		if (principal != null) {
			MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			int loggedInMemberId = loggedInMember.getMember().getId();
			List<OrderItem> orderItemList = orderItemRepository.findAllByMemberId(loggedInMemberId);
			List<CartItem> cartItemList = cartItemRepository.findAllByMemberId(loggedInMemberId);

			int count = 0;
			for (CartItem cartItem : cartItemList) {

				count++;
			}

			model.addAttribute("count", count);
			model.addAttribute("orderItemList", orderItemList);
			List<Category> categoryList = categoryRepository.findAll();
			model.addAttribute("categoryList", categoryList);
			
			
			
		}

		return "view_history";
	}

}