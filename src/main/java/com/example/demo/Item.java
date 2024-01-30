package com.example.demo;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Item {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@NotEmpty(message="Name cannot be empty!")
	@Size(min=5 ,max=50, message="Name length must be 5-50")
	private String name;
	
	@NotNull
	@NotEmpty(message="Description cannot be empty!")
	@Size(min=4 ,max=500, message="Name length must be 4-500")
	private String description;
	
	@DecimalMin(value = "0.1",message="Price should be positive numerical value!")
	private double price;
	@Min(value = 1,message="Quantity should be positive whole number!")
	@Max(value=100000000)
	private int quantity;
	
	private String imgName;
	
	@ManyToOne
	@JoinColumn(name="category_id",nullable=false)
	private Category category;
	
	@OneToMany(mappedBy="item", cascade=CascadeType.ALL)
	private Set<CartItem> items;
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
}
