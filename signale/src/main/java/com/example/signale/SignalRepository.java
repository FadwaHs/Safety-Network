package com.example.signale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SignalRepository extends MongoRepository<Signal,Long>{
    @Query(value = "{}", sort = "{ 'signalId' : -1 }", fields = "{ 'signalId' : 1 }")
    List<Signal> findMaxSignalId();

    List<Signal> findByUserId(Long id);
}
