package com.hadiyasa.springRestfulAPI.service;

import com.hadiyasa.springRestfulAPI.entity.User;
import com.hadiyasa.springRestfulAPI.model.request.CreateAddressRequest;
import com.hadiyasa.springRestfulAPI.model.request.UpdateAddressRequest;
import com.hadiyasa.springRestfulAPI.model.response.AddressResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(User user, CreateAddressRequest createAddressRequest);
    AddressResponse getAddress(User user, String contactId, String addressId);
    AddressResponse updateAddress(User user, UpdateAddressRequest updateAddressRequest);
    void removeAddress(User user, String contactId, String addressId);
    List<AddressResponse> listAddresses(User user, String contactId);
}
