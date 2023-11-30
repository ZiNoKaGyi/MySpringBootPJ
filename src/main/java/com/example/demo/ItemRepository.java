package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {



public	List<Item> findAllByCategory(Category referenceById);

public List<Item> findAllByCategoryId(Integer id);



}
