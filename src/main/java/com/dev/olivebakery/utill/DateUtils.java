package com.dev.olivebakery.utill;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by YoungMan on 2019-05-21.
 */

public class DateUtils {

  public static LocalDateTime getStartOfToday() {
    return LocalDate.now().atStartOfDay();
  }

  public static LocalDateTime getEndOfToday() {
    return LocalDate.now().atTime(23, 59, 59);
  }

  public static LocalDateTime getStartOfDay(LocalDate date) {
    return date.atStartOfDay();
  }

  public static LocalDateTime getEndOfDay(LocalDate date) {
    return date.atTime(23, 59, 59);
  }
}
