package com.codepied.api.api

import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Time Service for Global Service (not yet)
 *
 * @author Aivyss
 * @since 12/02/2022
 */
@Service
class TimeService {
    fun now(): ZonedDateTime {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
    }
}