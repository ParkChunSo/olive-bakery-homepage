package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.enums.DayType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="bread_day_tbl")
@Builder
public class Days {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "days_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private DayType dayType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bread_id")
    private Bread bread;

    public static List<Days> newListInstance(Bread bread, List<DayType> dayTypeList){
        List<Days> days = new ArrayList<>();
        for(DayType type : dayTypeList){
            days.add(
                    Days.builder().bread(bread).dayType(type).build()
            );
        }
        return days;
    }
}
