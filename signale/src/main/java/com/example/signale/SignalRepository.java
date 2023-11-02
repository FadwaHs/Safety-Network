package com.example.signale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SignalRepository extends MongoRepository<Signal,Long>{
    @Query(value = "{}", sort = "{ 'signalId' : -1 }", fields = "{ 'signalId' : 1 }")
    Signal findMaxSignalId();

}
