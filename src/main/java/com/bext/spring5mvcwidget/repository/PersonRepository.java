package com.bext.spring5mvcwidget.repository;

import com.bext.spring5mvcwidget.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface PersonRepository extends CrudRepository<Person, Long> {
}
