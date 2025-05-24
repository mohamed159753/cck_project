package com.pfe.Reservation_Bill_Management.services.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class EmailChangeService {
    // Temporary in-memory storage for new emails keyed by professor ID
    private Map<Long, String> newEmails = new ConcurrentHashMap<>();

    public void storeNewEmail(Long professorId, String newEmail) {
        newEmails.put(professorId, newEmail);
    }

    public String getNewEmail(Long professorId) {
        return newEmails.get(professorId);
    }

    public void clearNewEmail(Long professorId) {
        newEmails.remove(professorId);
    }
}