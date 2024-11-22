package com.hadiyasa.springRestfulAPI.repository;

import com.hadiyasa.springRestfulAPI.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
}
