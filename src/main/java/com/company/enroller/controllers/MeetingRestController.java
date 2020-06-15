package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
            Meeting foundMeeting = meetingService.findById(meeting.getId());
            if (foundMeeting != null) {
                    return new ResponseEntity(
                                    "Unable to create. A meeting with id " + meeting.getId() + " already exist.",
                                    HttpStatus.CONFLICT);
            }
            meetingService.add(meeting);
            return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delMeeting(@PathVariable("id") long id) {
            Meeting foundMeeting = meetingService.findById(id);
            if (foundMeeting == null) {
                    return new ResponseEntity("Unable to delete non-existant meeting. Meeting with id: " + id + " does not exist", HttpStatus.NOT_FOUND);
            }
            meetingService.delete(foundMeeting);
            return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(@PathVariable("id") long id) {
            Meeting meeting = meetingService.findById(id);
            if (meeting == null) {
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
    }
    

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipant(@PathVariable("id") long id, @PathVariable("login") String login) {
            Meeting meeting = meetingService.findById(id);
            Participant foundParticipant = participantService.findByLogin(login);
            
            if (meeting == null) {
                    return new ResponseEntity("Unable to add the participant to a non-existing meeting. A meeting with an id " + id + " doesn't exist.", HttpStatus.NOT_FOUND);
            }
            if (foundParticipant == null) {
                    return new ResponseEntity("Unable to add the participant to the meeting. A participant with login " + login + " doesn't exist.", HttpStatus.NOT_FOUND);
            }
            meeting.addParticipant(foundParticipant);
            meeting = meetingService.update(meeting);
            return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") long id, @PathVariable("login") String login)  {
            Meeting foundMeeting = meetingService.findById(id);
            Participant foundParticipant = participantService.findByLogin(login);
            if (foundMeeting == null) {
                    return new ResponseEntity("Meeting with id: " + id + " does not exist", HttpStatus.NOT_FOUND);
            }
            if (foundParticipant == null) {
                    return new ResponseEntity("Participant with login: " + login + " does not exist", HttpStatus.NOT_FOUND);
            }
            foundMeeting.removeParticipant(foundParticipant);
            foundMeeting = meetingService.update(foundMeeting);
            return new ResponseEntity<Collection<Participant>>(foundMeeting.getParticipants(), HttpStatus.OK);
    }
}
