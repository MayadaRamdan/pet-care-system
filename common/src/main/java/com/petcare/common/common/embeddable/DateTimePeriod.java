package com.petcare.common.common.embeddable;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class DateTimePeriod implements Period<LocalDateTime> {

  private LocalDateTime start;
  private LocalDateTime end;

  public static DateTimePeriod of(LocalDateTime start, LocalDateTime end) {
    DateTimePeriod dateTimePeriod = new DateTimePeriod();
    dateTimePeriod.start = start;
    dateTimePeriod.end = end;
    return dateTimePeriod;
  }
}
