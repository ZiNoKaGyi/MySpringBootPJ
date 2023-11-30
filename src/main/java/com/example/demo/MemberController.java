package com.example.demo;

import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class MemberController {
	
	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("/memberadd")
	public String addMember(Model model) {
		model.addAttribute("member", new Member());
		return "add_member";
	}

	@PostMapping("/membersave")
	public String saveMember(@Valid Member member, BindingResult bindingResult,RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "add_member";
		}
		System.out.println(member.getRole()+"************************");
		if(member.getRole()==null) {
			member.setRole("ROLE_USER");
		}
		BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
		String encodePassword =bCryptPasswordEncoder.encode(member.getPassword());
		member.setPassword(encodePassword);
		memberRepository.save(member);
		redirectAttributes.addFlashAttribute("success","Member Registered");
		return "redirect:/members";
	}

	@GetMapping("/members")
	public String viewItems(Model model) {
		
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
		
		Category category=new Category();
		model.addAttribute("category",category);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		List<Member> memberList = memberRepository.findAll();
		model.addAttribute("memberList", memberList);
		return "view_member";
	}
	

	@GetMapping("/members{id}")
	public String viewSingleitem(@PathVariable("id") Integer id, Model model) {
		Member member = memberRepository.getReferenceById(id);
		model.addAttribute("member", member);
		return "view_single_member";
	}

	@GetMapping("/memberedit{id}")
	public String editMember(@PathVariable("id") Integer id, Model model) {
		Member member = memberRepository.getReferenceById(id);
		model.addAttribute("member", member);
		List<Member> memberList =memberRepository.findAll();
		model.addAttribute(memberList);
		return "edit_member";
	}

	@PostMapping("/member/edit{id}")
	public String saveMember( Member member) {
		memberRepository.save(member);
		return "redirect:/members";
	}

	@GetMapping("/member/delete/{id}")
	public String deleteMember(@PathVariable("id") Integer id) {
		memberRepository.deleteById(id);
		return "redirect:/members";

	}
}
