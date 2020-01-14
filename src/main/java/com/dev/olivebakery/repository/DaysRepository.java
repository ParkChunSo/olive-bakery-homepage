package com.dev.olivebakery.repository;

import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.Days;
import com.dev.olivebakery.domain.enums.DayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface DaysRepository extends JpaRepository<Days, Long> {

    @Query("select d from Days d where d.dayType = :dayType")
    List<Days> findByDayType(@Param(value="dayType")DayType dayType);

    @Query("select d.dayType from Days d where d.bread = :bread")
    List<DayType> findByBread(@Param(value = "bread") Bread bread);

    @Transactional
    @Modifying
    @Query("delete from Days d where d.bread = :bread")
    void deleteByBread(@Param(value = "bread") Bread bread);
}