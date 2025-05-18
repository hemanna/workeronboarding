package com.sbd.common.util;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

import java.util.UUID;

@ApplicationScoped
@AllArgsConstructor
public class OnBoardingUtil {
    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
