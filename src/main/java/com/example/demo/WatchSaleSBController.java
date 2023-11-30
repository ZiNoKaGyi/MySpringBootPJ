package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WatchSaleSBController {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("/about")
	public String about(Model model) {
		
		// Get currently logged in user
				MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				int loggedInMemberId = loggedInMember.getMember().getId();// member id

				// Get shopping cart items added by this user
				// *Hint: You will need to use the method we added in the CartItemRepository
				List<CartItem> cartItemList = cartItemRepository.findAllByMemberId(loggedInMemberId);

				// Add the shopping cart items to the model
				model.addAttribute("cartItemList", cartItemList);
				
				

				// Calculate the total cost of all items in the shopping cart
				// item1=>cartitem.item.price*cartitem.quantity=123*2=246;
				// item3=>cartitem.item.price*cartitem.quantity=123*4=492;
				// total=>738;
				/* double total = 0; */
				int count=0;
				for (CartItem cartItem : cartItemList) {
//					cartItem.setSubtotal(cartItem.getItem().getPrice() * cartItem.getQuantity());
//					total += cartItem.getSubtotal();
					count++;
				}

				// Add the shopping cart total to the model

//				model.addAttribute("total", total);
				model.addAttribute("count", count);
				List<Category> categoryList = categoryRepository.findAll();
				model.addAttribute("categoryList", categoryList);
		
		return "about";
	}
	
	
	
	
	@GetMapping("/")
	public String index(Model model) {
		
		// Get currently logged in user
				MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				int loggedInMemberId = loggedInMember.getMember().getId();// member id

				// Get shopping cart items added by this user
				// *Hint: You will need to use the method we added in the CartItemRepository
				List<CartItem> cartItemList = cartItemRepository.findAllByMemberId(loggedInMemberId);

				// Add the shopping cart items to the model
				model.addAttribute("cartItemList", cartItemList);
				
				

				// Calculate the total cost of all items in the shopping cart
				// item1=>cartitem.item.price*cartitem.quantity=123*2=246;
				// item3=>cartitem.item.price*cartitem.quantity=123*4=492;
				// total=>738;
				/* double total = 0; */
				int count=0;
				for (CartItem cartItem : cartItemList) {
//					cartItem.setSubtotal(cartItem.getItem().getPrice() * cartItem.getQuantity());
//					total += cartItem.getSubtotal();
					count++;
				}

				// Add the shopping cart total to the model

//				model.addAttribute("total", total);
				model.addAttribute("count", count);
		
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		return "login";
	}
	
	
	
	
	
	
}
