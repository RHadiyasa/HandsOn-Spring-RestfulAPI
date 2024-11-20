package com.hadiyasa.springRestfulAPI.repository;

import com.hadiyasa.springRestfulAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
