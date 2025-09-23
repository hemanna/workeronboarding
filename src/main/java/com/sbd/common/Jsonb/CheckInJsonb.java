package com.sbd.common.Jsonb;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckInJsonb {
    private LocalTime checkIn;
}
