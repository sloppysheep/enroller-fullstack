package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
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
}
