package com.trendify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

// This class represents the product categories table in the database
@Entity
@Table(name = "categories")
@Data
public class Category {
    
    // Unique ID for each category
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Category name cannot be null and maximum length is 50 characters
    @NotNull
    @Size(max = 50)
    private String name;
    
    // Self-referencing relationship: Many sub-categories can belong to one parent category
    // FetchType.EAGER means the parent details are loaded immediately with the sub-category
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    // Defines the depth level of the category (e.g., 1 for Top, 2 for Sub-category, 3 for Item-level)
    private int level;

}
