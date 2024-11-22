package com.hadiyasa.springRestfulAPI.service;

import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.CreateContactRequest;
import com.hadiyasa.springRestfulAPI.model.request.SearchContactRequest;
import com.hadiyasa.springRestfulAPI.model.request.UpdateContactRequest;
import com.hadiyasa.springRestfulAPI.model.response.ContactResponse;
import org.springframework.data.domain.Page;

public interface ContactService {
    ContactResponse createContact(User user, CreateContactRequest createContactRequest);
    ContactResponse getContact(User user, String id);
    ContactResponse updateContact(User user, UpdateContactRequest updateContactRequest);
    void deleteContact(User user, String id);
    Page<ContactResponse> searchContact(User user, SearchContactRequest searchContactRequest);
}
