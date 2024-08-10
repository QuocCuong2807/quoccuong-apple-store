package com.springteam.backend.repository;

import com.springteam.backend.entity.ProductRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRatingRepository extends JpaRepository<ProductRating, Long> {
    Page<ProductRating> findAllByProductId(Long id, Pageable pageable);

    @Query("SELECT AVG(r.star) FROM ProductRating r WHERE r.product.id = :id")
    float finAverageStarByProductId(@Param("id") Long productId);

    @Query("select count(*) as quantity " +
            "FROM ProductRating r " +
            "where r.product.id =:id and r.star = :star")
    int findStarOverviewByProductId(@Param("id") Long productId, @Param("star") int star);

    long countAllByProductId(Long productId);
}
