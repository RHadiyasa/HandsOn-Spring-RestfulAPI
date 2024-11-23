package com.hadiyasa.springRestfulAPI.repository;

import com.hadiyasa.springRestfulAPI.entity.Address;
import com.hadiyasa.springRestfulAPI.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    Optional<Address> findFirstByContactAndId(Contact contact, String address);
    List<Address> findAllByContact(Contact contact);
}
