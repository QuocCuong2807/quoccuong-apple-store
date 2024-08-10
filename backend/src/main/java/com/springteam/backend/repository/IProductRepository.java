package com.springteam.backend.repository;

import com.springteam.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category.id = :category")
    Page<Product> findProductsByCategoryIdOrderByPrice(@Param("category") Long filteredCategory, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :category and p.name LIKE CONCAT('%', :series, '%')")
    Page<Product> findProductBySeriesAndCategory(@Param("series") String productSeries, @Param("category") Long categoryId, Pageable pageable);

    List<Product> findFirst4ByCategoryId(Long id);

    @Query("SELECT p FROM Product p WHERE p.name like CONCAT('%', :name, '%')")
    List<Product> findProductsByName(@Param("name") String productName);
}
