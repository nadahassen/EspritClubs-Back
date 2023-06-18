package com.stage.spring.service;
import com.stage.spring.entity.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
public interface IServiceEvent {

    List<Event> getAllEvents();

    Event getEventById(Long eventId);

    Event createEvent(Event event, MultipartFile image) throws IOException;

    Event updateEvent(Event event, MultipartFile image) throws IOException;

    void deleteEvent(Long eventId);

    List<Event> getEventsByClubId(Long clubId);

  //  List<Event> getUndeletedEvents();

    void addParticipant(Long eventId, Long userId);

    void removeParticipant(Long eventId, Long userId);

    List<Long> getEventParticipants(Long eventId);





}
