package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class CategoryController {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ItemRepository itemRepository;

	@GetMapping("/category")
	public String category(Model model) {
		
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
//			cartItem.setSubtotal(cartItem.getItem().getPrice() * cartItem.getQuantity());
//			total += cartItem.getSubtotal();
			count++;
		}

		// Add the shopping cart total to the model

//		model.addAttribute("total", total);
		model.addAttribute("count", count);

		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);

		return "view_category";
	}

	@GetMapping("/categoryadd")
	public String addCategory(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("category", new Category());
		return "add_category";
	}

	@PostMapping("/categorysave")
	public String saveCategory(@Valid Category c, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "add_category";
		}
		categoryRepository.save(c);
		return "redirect:/category";
	}

	@GetMapping("/categoryedit{id}")
	public String editCategory(@PathVariable("id") Integer id, Model model) {
		Category category = categoryRepository.getReferenceById(id);
		model.addAttribute("category", category);
		return "edit_category";

	}

	@PostMapping("/categoryedit{id}")
	public String saveUpdatedCategoy(Category category) {

		return "redirect:/category";
	}

	@GetMapping("/categorydelete{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {
		categoryRepository.deleteById(id);
		return "redirect:/category";

	}
	
	

	@GetMapping("/header")
	public String header(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		return "fragments/header";
	}

	@GetMapping("/products{id}")
	public String productsById(@PathVariable("id") Integer id, Model model) {
		
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		
		List<CartItem>cartItemList=cartItemRepository.findAllByMemberId(loggedInMemberId);
		int count=0;
		for(CartItem cartItem:cartItemList) {
			count++;
		}
		
		model.addAttribute("count",count);
		
		List<Item> itemList = itemRepository.findAllByCategory(categoryRepository.getReferenceById(id));
		model.addAttribute("itemList", itemList);
		
		List<Category>categoryList=categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		
		return "view_products";

	}

}
