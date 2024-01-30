package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TierController {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private TierRepository tierRepository;
	
	@GetMapping("/tier")
	public String tier(Model model,Tier tier) {
		
		
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		
		List<Member> memberList=memberRepository.findAll();
		model.addAttribute("memberList", memberList);
		
		return "tier";
		
	}

}
