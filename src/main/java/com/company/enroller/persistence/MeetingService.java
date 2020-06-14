package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    public Meeting findById(long id) {
            return (Meeting) connector.getSession().get(Meeting.class, id);
    }
    
    public Meeting add(Meeting meeting) {
            Transaction transaction = this.connector.getSession().beginTransaction();
            connector.getSession().save(meeting);
            transaction.commit();
            return meeting;
    }
    
    public void delete(Meeting meeting) {
        Transaction transaction = this.connector.getSession().beginTransaction();
        connector.getSession().delete(meeting);
        transaction.commit();
}


}
