package com.dev.olivebakery.domain.dtos.reservation;

import com.dev.olivebakery.domain.enums.ReservationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationDateRequestDto {
    private ReservationType reservationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @ApiModelProperty(notes = "2019-04-14 같은 형태.")
    private LocalDate selectDate;

    @Builder
    public ReservationDateRequestDto(ReservationType reservationType, LocalDate selectDate) {
        this.reservationType = reservationType;
        this.selectDate = selectDate;
    }
}
