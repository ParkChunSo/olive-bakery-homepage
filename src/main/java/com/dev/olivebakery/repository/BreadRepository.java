package com.dev.olivebakery.repository;

import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.enums.DayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by YoungMan on 2019-02-09.
 */

public interface BreadRepository extends JpaRepository<Bread, Long> {


    @Query("select b from Bread b where b.name = :name and b.deleteFlag = false")
    Optional<Bread> findByName(@Param(value="name")String name);


    List<Bread> findByNameIn(List<String> breadName);
}
