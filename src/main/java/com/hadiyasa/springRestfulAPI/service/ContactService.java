package com.hadiyasa.springRestfulAPI.service;

import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.CreateContactRequest;
import com.hadiyasa.springRestfulAPI.model.response.ContactResponse;

public interface ContactService {
    ContactResponse createContact(User user, CreateContactRequest createContactRequest);
}
