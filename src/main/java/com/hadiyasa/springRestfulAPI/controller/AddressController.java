package com.hadiyasa.springRestfulAPI.controller;

import com.hadiyasa.springRestfulAPI.entity.Contact;
import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.CreateAddressRequest;
import com.hadiyasa.springRestfulAPI.model.request.UpdateAddressRequest;
import com.hadiyasa.springRestfulAPI.model.response.AddressResponse;
import com.hadiyasa.springRestfulAPI.model.response.WebResponse;
import com.hadiyasa.springRestfulAPI.service.AddressService;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
public class AddressController {

    private final AddressService addressService;
    private final ConfigurationPropertiesAutoConfiguration configurationPropertiesAutoConfiguration;

    public AddressController(AddressService addressService, ConfigurationPropertiesAutoConfiguration configurationPropertiesAutoConfiguration) {
        this.addressService = addressService;
        this.configurationPropertiesAutoConfiguration = configurationPropertiesAutoConfiguration;
    }

    @PostMapping(path = "/api/contacts/{contactId}/addresses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<AddressResponse> create(User user,
                                               @PathVariable(name = "contactId") String id,
                                               @RequestBody CreateAddressRequest createAddressRequest) {
        createAddressRequest.setContactId(id);
        AddressResponse addressResponse = addressService.createAddress(user, createAddressRequest);

        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @GetMapping(path = "/api/contact/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<AddressResponse> getAddress(@PathVariable(name = "contactId") String contactId,
                                                   @PathVariable(name = "addressId") String addressId,
                                                   User user) {
        AddressResponse addressResponse = addressService.getAddress(user, contactId, addressId);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @PutMapping(path = "/api/contact/{contactId}/addresses/{addressId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<AddressResponse> updateAddress(User user,
                                                      @PathVariable(name = "contactId") String contactId,
                                                      @PathVariable(name = "addressId") String addressId,
                                                      @RequestBody UpdateAddressRequest updateAddressRequest) {
        // set addressId dan contactId based on parameter
        updateAddressRequest.setAddressId(addressId);
        updateAddressRequest.setContactId(contactId);

        AddressResponse addressResponse = addressService.updateAddress(user, updateAddressRequest);

        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @DeleteMapping(path = "/api/contact/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<String> deleteAddress(User user,
                                             @PathVariable(name = "contactId") String contactId,
                                             @PathVariable(name = "addressId") String addressId) {
        addressService.removeAddress(user, contactId, addressId);

        return WebResponse.<String>builder().data("Contact Deleted").build();
    }

    @GetMapping(path = "/api/contact/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<List<AddressResponse>> listAddresses(User user,
                                                            @PathVariable(name = "contactId") String contactId) {
        List<AddressResponse> addressResponses = addressService.listAddresses(user, contactId);
        return WebResponse.<List<AddressResponse>>builder().data(addressResponses).build();
    }
}
