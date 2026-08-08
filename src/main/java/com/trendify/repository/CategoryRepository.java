package com.trendify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.trendify.entity.Category;

// This interface manages database operations for product Categories
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	// Finds a category directly by using its unique name
	public Category findByName(String name);

	// Custom query to find a sub-category matching a specific name and its parent category's name
	@Query("Select c from Category c where c.name=:name AND c.parentCategory.name=:parentCategoryName")
	public Category findByNameAndParant(@Param("name") String name, @Param("parentCategoryName") String parentCategoryName);
}

