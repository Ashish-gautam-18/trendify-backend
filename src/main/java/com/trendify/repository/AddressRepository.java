package com.trendify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.trendify.entity.Address;

// This interface provides built-in database operations (Save, Update, Delete, Find) for the Address entity
public interface AddressRepository extends JpaRepository<Address, Long> {

}
