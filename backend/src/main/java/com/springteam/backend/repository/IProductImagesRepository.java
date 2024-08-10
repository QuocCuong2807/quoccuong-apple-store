package com.springteam.backend.repository;

import com.springteam.backend.entity.ProductImages;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductImagesRepository extends JpaRepository<ProductImages, Long> {
    @Transactional
    void deleteAllByProductId(Long productId);
}
