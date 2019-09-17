package com.dev.olivebakery.repository;

import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.repository.custom.BreadRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by YoungMan on 2019-02-09.
 */

@Transactional
public interface BreadRepository extends JpaRepository<Bread, Long>, BreadRepositoryCustom {

    @Query("select b from Bread b where b.name = :name and b.isDeleted = false")
    Optional<Bread> findByName(@Param(value="name")String name);

    @Query("select b from Bread b where b.isDeleted = false")
    List<Bread> findAllByDeleteFlagIsFalse();

    @Query("select b from Bread b where b.name in :names")
    List<Bread> findAllByByNameInQuery(@Param(value = "names") List<String> names);
}
