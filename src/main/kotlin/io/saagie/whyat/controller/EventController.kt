package io.saagie.whyat.controller

import io.saagie.whyat.domain.Event
import io.saagie.whyat.service.EventService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController(val eventService: EventService) {

    @PostMapping("/event")
    fun recordEvent(@RequestBody event: Event): Event {
        return eventService.recordEvent(event)
    }
}