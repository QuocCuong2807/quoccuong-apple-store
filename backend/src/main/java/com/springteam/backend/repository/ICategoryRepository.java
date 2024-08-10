package com.springteam.backend.repository;

import com.springteam.backend.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<ProductCategory, Long> {
    @Query("SELECT c from ProductCategory c WHERE c.name LIKE CONCAT('%', :name, '%')")
    Page<ProductCategory> findAllByName(String name,Pageable pageable);

}
