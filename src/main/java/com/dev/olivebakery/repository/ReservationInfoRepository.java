package com.dev.olivebakery.repository;

import com.dev.olivebakery.domain.entity.ReservationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by YoungMan on 2019-02-09.
 */

public interface ReservationInfoRepository extends JpaRepository<ReservationInfo, Long> {

	@Transactional
	@Modifying
	@Query("delete from ReservationInfo r where r.id in :ids")
	void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
