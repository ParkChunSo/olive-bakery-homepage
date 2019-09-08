package com.dev.olivebakery.domain.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSaveRequestDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime bringTime;
    private List<ReservationBreadDto> breadInfo = new ArrayList<>();

    @Builder
    public ReservationSaveRequestDto(LocalDateTime bringTime, List<ReservationBreadDto> breadInfo) {
        this.bringTime = bringTime;
        this.breadInfo = breadInfo;
    }

    @ApiModelProperty(hidden = true)
    public List<String> getBreadNames() {
        return breadInfo.stream()
                .map(s -> s.getBreadName())
                .collect(Collectors.toList());
    }

    @ApiModelProperty(hidden = true)
    public List<Integer> getBreadCounts() {
        return breadInfo.stream()
                .map(s -> s.getBreadCount())
                .collect(Collectors.toList());
    }
}
