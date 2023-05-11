package com.airbyte.dorm.phonebook;

import com.airbyte.dorm.model.PhoneBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneBookRepository extends JpaRepository <PhoneBook , String> {
}
