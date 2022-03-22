package com.example.Recipe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {



    User findByEmail(String email);





}
