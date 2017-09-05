package com.library.app.commontests.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;

import com.library.app.category.model.Category;

@Ignore
public class CategoryForTestsRepository {

	public static Category java() {
		return new Category("Java");
	}
	
	public static Category cleanCode() {
		return new Category("Clean Code");
	}
	
	public static Category android() {
		return new Category("Android");
	}
	
	public static Category network() {
		return new Category("Network");
	}
	
	public static Category categoryWithId(Category category, long categoryId) {
		category.setId(categoryId);
		return category;
	}
	
	public static List<Category> allCategories(){
		return new ArrayList<>(Arrays.asList(java(), cleanCode(), android(), network()));
	}
}
