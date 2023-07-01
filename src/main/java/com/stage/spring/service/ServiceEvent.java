package com.stage.spring.service;

import com.stage.spring.entity.ClubPublication;
import com.stage.spring.entity.Event;
import com.stage.spring.entity.Image;
import com.stage.spring.entity.User;
import com.stage.spring.repository.EventRepository;
import com.stage.spring.repository.ImageRepository;
import com.stage.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.stage.spring.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.util.List;
@Service
@Slf4j
public class ServiceEvent implements IServiceEvent {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IServiceImage serviceImage;
    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();

    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    /*@Override
    public Event createEvent(Event event, MultipartFile image) throws IOException {
        if (image != null) {
            Image savedImage = serviceImage.addImage(new Image(image.getOriginalFilename()));
            serviceImage.save(image);
            event.setImage(savedImage);
        }
        return eventRepository.save(event);
    }*/
    public void createEvent(Event f, Long idu, MultipartFile file) throws IOException {

        List<String> filenames = new ArrayList<>();

        f.setOrganizer(userRepository.findById(idu).orElse(null));
        //Image
        Image image = new Image(file.getOriginalFilename());
        String filename1 = StringUtils.cleanPath(file.getOriginalFilename());
        filenames.add(filename1);
        f.setImage(image);
        serviceImage.save(file);
        eventRepository.save(f);

    }


    @Override
    public Event updateEvent(Event event, MultipartFile image) throws IOException {
        if (image != null) {
            Image savedImage = serviceImage.addImage(new Image(image.getOriginalFilename()));
            serviceImage.save(image);
            event.setImage(savedImage);
        }
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);

    }

    @Override
    public List<Event> getEventsByClubId(Long clubId) {

        return eventRepository.findByClubs_IdClub(clubId);
    }

   /* @Override
    public List<Event> getUndeletedEvents() {

        return eventRepository.getUndeletedEvents();
    }*/

    @Override
    public void addParticipant(Long eventId, Long userId) {
       /* Event event = eventRepository.findById(eventId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (event != null && user != null && user.getRoleU().contains(Role.ETUDIANT)) {
            event.getParticipants().add(user);
            eventRepository.save(event);
        }
    }*/
    }

    @Override
    public void removeParticipant(Long eventId, Long userId) {

    }

    @Override
    public List<Long> getEventParticipants(Long eventId) {

        return null;
    }
}