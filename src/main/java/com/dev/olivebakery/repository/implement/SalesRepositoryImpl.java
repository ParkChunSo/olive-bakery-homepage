package com.dev.olivebakery.repository.implement;

import com.dev.olivebakery.domain.dtos.SalesDto;
import com.dev.olivebakery.domain.entity.QSales;
import com.dev.olivebakery.domain.entity.Sales;
import com.dev.olivebakery.repository.custom.SalesRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Component
public class SalesRepositoryImpl extends QuerydslRepositorySupport implements SalesRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    QSales sales = QSales.sales1;

    public SalesRepositoryImpl() {
        super(Sales.class);
    }

    @Override
    public List<SalesDto.GetGraphTmp> getAverageSales(String dayType, LocalDate date) {
        JPAQuery<SalesDto.GetGraphTmp> query = setGraphQuery(dayType, date);
        return query.fetch();
    }

    @Override
    public List<SalesDto.GetDashBoardTmp> getDashData(LocalDate date) {
        JPAQuery<SalesDto.GetDashBoardTmp> query = new JPAQuery<>(entityManager);
        query.select(Projections.constructor(SalesDto.GetDashBoardTmp.class, sales.date, sales.sales, sales.reservationCnt, sales.saleType))
                .from(sales)
                .where(sales.date.year().eq(date.getYear()))
                .where(sales.date.month().eq(date.getMonthValue()))
                .groupBy(sales.date, sales.saleType);
        return query.fetch();
    }

    private JPAQuery<SalesDto.GetGraphTmp> setGraphQuery(String DayType, LocalDate date){
        JPAQuery<SalesDto.GetGraphTmp> query = new JPAQuery<>(entityManager);
        query.select(Projections.constructor(SalesDto.GetGraphTmp.class, sales.date, sales.sales.avg(), sales.saleType))
                .from(sales)
                .orderBy(sales.date.desc());
        if(DayType.equals("YEAR")){
            query.groupBy(sales.date.year(), sales.saleType);
        } else if (DayType.equals("MONTH")) {
            query.where(sales.date.year().eq(date.getYear()))
                    .groupBy(sales.date.month(), sales.saleType);
        } else {
            query.where(sales.date.year().eq(date.getYear()))
                    .where(sales.date.month().eq(date.getMonthValue()))
                    .groupBy(sales.date, sales.saleType);
        }
        return query;
    }
}
