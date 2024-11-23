package com.hadiyasa.springRestfulAPI.service.impl;

import com.hadiyasa.springRestfulAPI.entity.Contact;
import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.CreateContactRequest;
import com.hadiyasa.springRestfulAPI.model.request.SearchContactRequest;
import com.hadiyasa.springRestfulAPI.model.request.UpdateContactRequest;
import com.hadiyasa.springRestfulAPI.model.response.ContactResponse;
import com.hadiyasa.springRestfulAPI.repository.ContactRepository;
import com.hadiyasa.springRestfulAPI.service.ContactService;
import com.hadiyasa.springRestfulAPI.service.ValidationService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ValidationService validationService;

    public ContactServiceImpl(ContactRepository contactRepository, ValidationService validationService) {
        this.contactRepository = contactRepository;
        this.validationService = validationService;
    }

    @Transactional
    @Override
    public ContactResponse createContact(User user, CreateContactRequest createContactRequest) {
        validationService.validate(createContactRequest);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(createContactRequest.getFirstName());
        contact.setLastName(createContactRequest.getLastName());
        contact.setEmail(createContactRequest.getEmail());
        contact.setPhone(createContactRequest.getPhoneNumber());
        contact.setUser(user);
        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    @Override
    public ContactResponse getContact(User user, String id) {
        Contact contact = contactRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact not found"));

        return toContactResponse(contact);
    }

    @Transactional
    @Override
    public ContactResponse updateContact(User user, UpdateContactRequest updateContactRequest) {
        validationService.validate(updateContactRequest);

        Contact contact = contactRepository.findByUserAndId(user, updateContactRequest.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstName(updateContactRequest.getFirstName());
        contact.setLastName(updateContactRequest.getLastName());
        contact.setEmail(updateContactRequest.getEmail());
        contact.setPhone(updateContactRequest.getPhoneNumber());
        contact.setUser(contact.getUser());
        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    @Override
    public void deleteContact(User user, String id) {
        Contact contact = contactRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contactRepository.delete(contact);
    }

    @Transactional
    @Override
    public Page<ContactResponse> searchContact(User user, SearchContactRequest searchContactRequest) {
        Specification<Contact> specification = ((root, query, criteriaBuilder) -> {
            // Predicate dari Jakarta Persistence
            List<Predicate> predicates = new ArrayList<>();

            /** Parameter 1 : User */
            predicates.add(criteriaBuilder.equal(root.get("user"), user));

            // cek
            if (Objects.nonNull(searchContactRequest.getName())){
                String lowerName = searchContactRequest.getName().toLowerCase();
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + lowerName + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lowerName + "%")
                ));
            }

            if (Objects.nonNull(searchContactRequest.getEmail())){
                String lowerEmail = searchContactRequest.getEmail().toLowerCase();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + lowerEmail + "%"));
            }
            if (Objects.nonNull(searchContactRequest.getPhone())){
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + searchContactRequest.getPhone() + "%"));
            }

            assert query != null;
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });

        Pageable pageable = PageRequest.of(searchContactRequest.getPage(), searchContactRequest.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream()
                .map(this::toContactResponse).toList();

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }

    private ContactResponse toContactResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .phoneNumber(contact.getPhone())
                .email(contact.getEmail())
                .build();
    }
}
