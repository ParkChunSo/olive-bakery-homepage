package com.dev.olivebakery.repository;

import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.BreadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BreadImageRepository extends JpaRepository<BreadImage, Long> {

    @Query(value = "select image from BreadImage image where image.current = true and image.bread = :bread")
    Optional<BreadImage> findByBread(Bread bread);
}