package com.hadiyasa.springRestfulAPI.service.impl;

import com.hadiyasa.springRestfulAPI.entity.Address;
import com.hadiyasa.springRestfulAPI.entity.Contact;
import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.CreateAddressRequest;
import com.hadiyasa.springRestfulAPI.model.request.UpdateAddressRequest;
import com.hadiyasa.springRestfulAPI.model.response.AddressResponse;
import com.hadiyasa.springRestfulAPI.repository.AddressRepository;
import com.hadiyasa.springRestfulAPI.repository.ContactRepository;
import com.hadiyasa.springRestfulAPI.service.AddressService;
import com.hadiyasa.springRestfulAPI.service.ValidationService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {
    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    private final ValidationService validationService;

    public AddressServiceImpl(AddressRepository addressRepository, ContactRepository contactRepository, ValidationService validationService) {
        this.addressRepository = addressRepository;
        this.contactRepository = contactRepository;
        this.validationService = validationService;
    }

    @Transactional
    @Override
    public AddressResponse createAddress(User user, CreateAddressRequest createAddressRequest) {
        validationService.validate(createAddressRequest);

        Contact contact = contactRepository.findByUserAndId(user, createAddressRequest.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        setAddressData(contact, address, createAddressRequest.getStreet(), createAddressRequest.getCity(), createAddressRequest.getProvince(), createAddressRequest.getCountry(), createAddressRequest.getPostalCode());

        return toAddressResponse(address);
    }

    @Transactional
    @Override
    public AddressResponse getAddress(User user, String contactId, String addressId) {
        Contact contact = contactRepository.findByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        return toAddressResponse(address);
    }

    @Transactional
    @Override
    public AddressResponse updateAddress(User user, UpdateAddressRequest updateAddressRequest) {
        validationService.validate(updateAddressRequest);

        Contact contact = contactRepository.findByUserAndId(user, updateAddressRequest.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, updateAddressRequest.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        setAddressData(
                contact,
                address,
                updateAddressRequest.getStreet(),
                updateAddressRequest.getCity(),
                updateAddressRequest.getProvince(),
                updateAddressRequest.getCountry(),
                updateAddressRequest.getPostalCode()
        );

        return toAddressResponse(address);
    }

    @Override
    public void removeAddress(User user, String contactId, String addressId) {

        Contact contact = contactRepository.findByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        addressRepository.delete(address);
    }

    @Transactional
    @Override
    public List<AddressResponse> listAddresses(User user, String contactId) {
        Contact contact = contactRepository.findByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        List<Address> addresses = addressRepository.findAllByContact(contact);

        return addresses.stream()
                .map(this::toAddressResponse)
                .toList();
    }

    private void setAddressData(Contact contact, Address address, String street, String city, String province, String country, String postalCode) {
        address.setStreet(street);
        address.setCity(city);
        address.setProvince(province);
        address.setCountry(country);
        address.setPostalCode(postalCode);
        address.setContact(contact);

        addressRepository.save(address);
    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }
}
