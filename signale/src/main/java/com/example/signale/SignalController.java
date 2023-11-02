package com.example.signale;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/signal")
public class SignalController {

    private SignalService signalService;

    @PostMapping
    public ResponseEntity<Signal> addSignal(@RequestBody Signal signal)
    {
        return new ResponseEntity<>(signalService.addSignal(signal), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<Signal>> getAllSignal(){
        return new ResponseEntity<>(signalService.getAllSignal(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public Optional<Signal> getSignlaById(@PathVariable Long id){

        return signalService.getSignal(id);
    }


    @PutMapping("{id}")
    public Signal updateSignalById(@PathVariable Long id, @RequestBody Signal signal){
        return signalService.updateSignal(id,signal);
    }

    @DeleteMapping("{id}")
    public void deleteSignalById(@PathVariable Long id){
        signalService.deleteSignal(id);
    }


}
