package com.dev.olivebakery.domain.dtos;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDto {

  private String userName;
  private String content;
  private Date date;

}
