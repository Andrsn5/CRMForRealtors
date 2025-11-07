package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.ClientDao;
import com.company.crm.model.Client;
import com.company.crm.service.interfaces.ClientService;
import com.company.crm.util.ValidationException;
import com.company.crm.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {
    private final ClientDao dao;

    public ClientServiceImpl(ClientDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Client> getAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Client> getById(int id) {
        if (id <= 0) {
            throw new ValidationException("Client ID must be positive");
        }
        return dao.findById(id);
    }

    @Override
    public Client create(Client client) {
        validateClient(client);
        return dao.save(client);
    }

    @Override
    public void update(Client client) {
        if (client.getId() == null || client.getId() <= 0) {
            throw new ValidationException("Valid client ID is required for update");
        }
        validateClient(client);
        dao.update(client);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Client ID must be positive");
        }
        dao.delete(id);
    }

    private void validateClient(Client client) {
        ValidationUtils.validateRequired(client.getFirstName(), "First name");
        ValidationUtils.validateRequired(client.getLastName(), "Last name");
        ValidationUtils.validateRequired(client.getClientType(), "Client type");

        ValidationUtils.validateEmail(client.getEmail());
        ValidationUtils.validatePhone(client.getPhone());

        // Валидация бюджета
        if (client.getBudget() != null) {
            ValidationUtils.validatePositive(client.getBudget(), "Budget");
            ValidationUtils.validateMinValue(client.getBudget(), BigDecimal.ZERO, "Budget");
        }

        ValidationUtils.validateMaxLength(client.getFirstName(), 100, "First name");
        ValidationUtils.validateMaxLength(client.getLastName(), 100, "Last name");
        ValidationUtils.validateMaxLength(client.getEmail(), 255, "Email");
        ValidationUtils.validateMaxLength(client.getClientType(), 50, "Client type");

        if (!isValidClientType(client.getClientType())) {
            throw new ValidationException("Invalid client type: " + client.getClientType() +
                    ". Allowed values: customer, seller, tenant, landlord");
        }
    }

    private boolean isValidClientType(String clientType) {
        return "customer".equals(clientType) ||
                "tenant".equals(clientType) ||
                "landlord".equals(clientType) ||
                "seller".equals(clientType);
    }
}