package com.example.demo;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FeedbackController {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Autowired
	private CartItemRepository cartItemRepo;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@GetMapping("/feedbacks")
	public String viewFeedbacks(Model model) {
		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();// member id

		// Get shopping cart items added by this user
		// *Hint: You will need to use the method we added in the CartItemRepository
		List<CartItem> cartItemList = cartItemRepo.findAllByMemberId(loggedInMemberId);

		int count = 0;
		for (CartItem cartItem : cartItemList) {
			count++;
		}

		// Add the shopping cart total to the model
		model.addAttribute("count", count);

		Category category = new Category();
		model.addAttribute("category", category);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		
		
		List<Feedback> feedbackList = feedbackRepository.findAll();
		model.addAttribute("feedbackList", feedbackList);
		return "view_feedbacks";
	}
	@GetMapping("/contact")
	public String contact(Model model) {

		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();// member id

		// Get shopping cart items added by this user
		// *Hint: You will need to use the method we added in the CartItemRepository
		List<CartItem> cartItemList = cartItemRepo.findAllByMemberId(loggedInMemberId);

		int count = 0;
		for (CartItem cartItem : cartItemList) {
			count++;
		}

		// Add the shopping cart total to the model
		model.addAttribute("count", count);

		Category category = new Category();
		model.addAttribute("category", category);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		
		
		List<Feedback> feedbackList = feedbackRepository.findAll();
		model.addAttribute("feedbackList", feedbackList);
		
		Member member=memberRepository.getReferenceById(loggedInMemberId);	
		model.addAttribute("member", member);
		
		
		Feedback feedback = new Feedback();
		model.addAttribute("feedback", feedback);
		return "contact";
	
	}
	@PostMapping("/feedbacksave")
	public String saveFeedback(Feedback feedback, Principal principal,RedirectAttributes redirectAttributes) {

		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Member member = loggedInMember.getMember();// member id) {
		feedback.setMember(member);
		feedbackRepository.save(feedback);
		redirectAttributes.addFlashAttribute("success", "Thank you for your feedback!");
		return "redirect:/contact";
	}
	
	@GetMapping("/reply{id}")
	public String replyBack(@PathVariable("id")int id) {
		
		Member member = memberRepository.getReferenceById(id);
		// Send email
		
		//String subject = "Order is confirmed!";

				String from = "thawzinoo6921@gmail.com";
				String body = "Thank you for your feedback!\n";
				String to =member.getEmail();

				sendEmail(to,body,from);
				List<Feedback> feedBackList =feedbackRepository.findAllByMemberId(member.getId());
				for(Feedback feedback :feedBackList) {
					feedback.setStatus(1);
					feedbackRepository.save(feedback);
				}
				
		return "redirect:/feedbacks";
	}
	
	public void sendEmail(String to, String body,String from) {
		SimpleMailMessage msg = new SimpleMailMessage();

		msg.setText(body);
		msg.setTo(to);
		msg.setFrom(from);
		

		javaMailSender.send(msg);
	}
	
	@GetMapping("/feedback/delete/{id}")
	public String deleteFeedback(@PathVariable("id") Integer id) {
		feedbackRepository.deleteById(id);
		return "redirect:/feedbacks";

	}
}