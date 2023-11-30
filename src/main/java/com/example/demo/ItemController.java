package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class ItemController {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/items")
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
		List<Item> itemList = itemRepository.findAll();// selcet * from category
		model.addAttribute("itemList", itemList);
		
		
		
		return "view_items";
	}

	@GetMapping("/itemadd")
	public String addItem(Model model) {
		Item item = new Item();
		model.addAttribute("item", item);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		return "additem";
	}

	@PostMapping("/itemsave")
	public String saveItem(@Valid Item item,BindingResult bindingResult, @RequestParam("itemImage") MultipartFile imgFile) {
		if(bindingResult.hasErrors()) {
			return "add_item";
		}
		String imageName = imgFile.getOriginalFilename();
		// set the image name in item object
		item.setImgName(imageName);
		// save the item obj to the db
		Item savedItem = itemRepository.save(item);
		try {
			// prepare the directory path
			String uploadDir = "uploads/items/" + savedItem.getId();
			Path uploadPath = Paths.get(uploadDir);
			// check if the upload path exists, if not it will be created
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			// prepare path for file
			Path fileToCreatePath = uploadPath.resolve(imageName);
			System.out.println("File path: " + fileToCreatePath);
			// copy file to the upload location
			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/items";
	}

	@GetMapping("/item{id}")
	public String viewSingleitem(@PathVariable("id") Integer id, Model model) {
		
		
		
		Item item = itemRepository.getReferenceById(id);
		model.addAttribute("item", item);
		Category category = categoryRepository.getReferenceById(id);
		model.addAttribute("category", category);
		
		return "view_single_item";
	}
	
	@GetMapping("/items{cid}")
	public String viewItemsByCategory(@PathVariable("cid") Integer id, Model model) {
		
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
		
		List<Item> itemList = itemRepository.findAllByCategoryId(id);
		model.addAttribute("itemList", itemList);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		/*
		 * Category category = categoryRepository.getReferenceById(id);
		 * model.addAttribute("category", category);
		 */
		return "view_items_category";
	}

	@GetMapping("/itemedit{id}")
	public String editItem(@PathVariable("id") Integer id, Model model) {
		Item item = itemRepository.getReferenceById(id);
		model.addAttribute("item", item);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute(categoryList);
		return "edit_item";

	}

	@PostMapping("/item/edit/{id}")
	public String saveUpdatedItem(@PathVariable("id") Integer id, Item item,
			@RequestParam("itemImage") MultipartFile imgFile) {
		String imageName = imgFile.getOriginalFilename();

		// get existing item from the database
		Item editItem = itemRepository.getReferenceById(id);

		if (imageName.isEmpty()) {
			// No new image selected, use the existing image name
			imageName = editItem.getImgName();
		}

		// set the image name in item object
		item.setImgName(imageName);

		// save the item obj to the db
		Item savedItem = itemRepository.save(item);
		try {
			// prepare the directory path
			String uploadDir = "uploads/items/" + savedItem.getId();
			Path uploadPath = Paths.get(uploadDir);

			// check if the upload path exists, if not it will be created
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// prepare path for file
			Path fileToCreatePath = uploadPath.resolve(imageName);

			// copy file to the upload location if a new image is provided
			if (!imgFile.isEmpty()) {
				Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/items";
	}

	@GetMapping("/item/delete/{id}")
	public String deleteItem(@PathVariable("id") Integer id) {
		itemRepository.deleteById(id);
		return "redirect:/items";

	}

}
