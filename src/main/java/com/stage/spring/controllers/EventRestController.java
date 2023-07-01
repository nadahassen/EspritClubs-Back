package com.stage.spring.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.spring.entity.ClubPublication;
import com.stage.spring.entity.Event;
import com.stage.spring.entity.Image;
import com.stage.spring.service.ServiceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/Event")
@CrossOrigin(origins = "http://localhost:4200")
public class EventRestController {

    @Autowired
    ServiceEvent serviceEvent;

    @GetMapping("/getAllEvents")
    public List<Event> getAllEvents() {
        return serviceEvent.getAllEvents();
    }

    @GetMapping("/getEvent/{eventId}")
    public Event getEventById(@PathVariable("eventId") Long eventId) {
        return serviceEvent.getEventById(eventId);
    }


   /* @PostMapping("/createEvent")
    public Event createEvent(@RequestBody Event event, @RequestParam(name = "image", required = false) MultipartFile image)
            throws IOException {
        return serviceEvent.createEvent(event, image);
    }
*/
    @PostMapping("/createEvent/{idu}")
    public void createEvent(@RequestParam("event") String event,
                        @RequestParam("file") MultipartFile file,

                        @PathVariable("idu") Long idu)
            throws IOException
    {
        Event f = new ObjectMapper().readValue(event, Event.class);
        serviceEvent.createEvent(f, idu,file);
    }

    @PutMapping("/updateEvent/{eventId}")
    public Event updateEvent(@PathVariable("eventId") Long eventId, @RequestBody Event event,
                             @RequestParam(name = "image", required = false) MultipartFile image)
            throws IOException {
        event.setIdEvent(eventId);
        return serviceEvent.updateEvent(event, image);
    }

    @DeleteMapping("/deleteEvent/{eventId}")
    public void deleteEvent(@PathVariable("eventId") Long eventId) {
        serviceEvent.deleteEvent(eventId);
    }

    @GetMapping("/getEventsByClubId/{clubId}")
    public List<Event> getEventsByClubId(@PathVariable("clubId") Long clubId) {
        return serviceEvent.getEventsByClubId(clubId);
    }

  /*  @GetMapping("/getUndeletedEvents")
    public List<Event> getUndeletedEvents() {
        return serviceEvent.getUndeletedEvents();
    }*/
}