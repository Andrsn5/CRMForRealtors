package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.ClientDao;
import com.company.crm.dao.interfaces.MeetingDao;
import com.company.crm.model.Client;
import com.company.crm.model.Meeting;
import com.company.crm.service.interfaces.ClientService;
import com.company.crm.service.interfaces.MeetingService;

import java.util.List;
import java.util.Optional;

public class MeetingServiceImpl implements MeetingService {
    private final MeetingDao dao;
    public MeetingServiceImpl(MeetingDao dao) { this.dao = dao; }

    @Override public List<Meeting> getAll() { return dao.findAll(); }
    @Override public Optional<Meeting> getById(int id) { return dao.findById(id); }
    @Override public Meeting create(Meeting e) { return dao.save(e); }
    @Override public void update(Meeting e) { dao.update(e); }
    @Override public void delete(int id) { dao.delete(id); }
}