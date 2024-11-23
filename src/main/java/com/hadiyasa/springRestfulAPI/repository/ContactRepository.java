package com.hadiyasa.springRestfulAPI.repository;

import com.hadiyasa.springRestfulAPI.entity.Contact;
import com.hadiyasa.springRestfulAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact> {
    /**
     * JpaSpesification : fitur yang disediakan oleh Spring Data JPA untuk membangun query dinamis berbasis kriteria
     * Contoh :
     * Kita ingin mencari contact bisa berdasarkan banyak parameter seperti nama, noTelp, email.
     * Kita bisa memanfaatkan JpaSpesification untuk membangun query yang dinamis berdasarkan parameter tersebut.
     * */

    Optional<Contact> findByUserAndId(User user, String id);
}
