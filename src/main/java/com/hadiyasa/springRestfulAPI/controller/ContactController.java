package com.hadiyasa.springRestfulAPI.controller;

import com.hadiyasa.springRestfulAPI.entity.Contact;
import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.CreateContactRequest;
import com.hadiyasa.springRestfulAPI.model.request.SearchContactRequest;
import com.hadiyasa.springRestfulAPI.model.request.UpdateContactRequest;
import com.hadiyasa.springRestfulAPI.model.response.ContactResponse;
import com.hadiyasa.springRestfulAPI.model.response.PagingResponse;
import com.hadiyasa.springRestfulAPI.model.response.WebResponse;
import com.hadiyasa.springRestfulAPI.service.ContactService;
import jakarta.persistence.PreUpdate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<ContactResponse> getContacts(User user, @PathVariable(name = "contactId") String id) {
        ContactResponse contactResponse = contactService.getContact(user, id);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @PutMapping(path = "/api/contacts/{contactId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<ContactResponse> updateContact(User user,
                                                      @PathVariable(name = "contactId") String id,
                                                      @RequestBody UpdateContactRequest updateContactRequest) {
        /** ID yang digunakan tidak boleh bersal dari RequestBody
         * Sehingga harus set ID pada request berdasarkan ID yang digunakan pada parameter URL.
         * Agar benar-benar aman, maka gunakan @JsonIgnore pada attribute id di dalam UpdateContactRequest
         * */
        updateContactRequest.setId(id);
        ContactResponse contactResponse = contactService.updateContact(user, updateContactRequest);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @DeleteMapping(path = "/api/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> deleteContact(User user,
                                             @PathVariable(name = "contactId") String id) {
        contactService.deleteContact(user, id);

        return WebResponse.<String>builder().data("OK").build();

    }

    @GetMapping(path = "/api/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<ContactResponse>> searchContact(User user,
                                                            @RequestParam(name = "name", required = false) String name,
                                                            @RequestParam(name = "email", required = false) String email,
                                                            @RequestParam(name = "phone", required = false) String phone,
                                                            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){
        SearchContactRequest contactRequest = SearchContactRequest.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .page(page)
                .size(size)
                .build();

        Page<ContactResponse> contactResponses = contactService.searchContact(user, contactRequest);
        return WebResponse.<List<ContactResponse>>builder().data(contactResponses.getContent()).pagingResponse(
                PagingResponse.builder()
                        .currentPage(contactResponses.getNumber())
                        .totalPage(contactResponses.getTotalPages())
                        .size(contactResponses.getSize())
                        .build()
        ).build();
    }

}
