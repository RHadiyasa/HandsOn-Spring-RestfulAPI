package com.hadiyasa.springRestfulAPI.controller;

import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.CreateContactRequest;
import com.hadiyasa.springRestfulAPI.model.response.ContactResponse;
import com.hadiyasa.springRestfulAPI.model.response.WebResponse;
import com.hadiyasa.springRestfulAPI.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping(path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest createContactRequest) {
        ContactResponse contactResponse = contactService.createContact(user, createContactRequest);

        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

}
