package com.example.signale;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Service
@AllArgsConstructor
public class SignalServiceImpl implements SignalService{

    private  SignalRepository signalRepository ;

    @Override
    public Signal addSignal(Signal signal) {

        return signalRepository.save(signal);
    }

    @Override
    public Optional<Signal> getSignal(Long id) {

        return Optional.of(signalRepository.findById(id).orElseThrow(()-> new RuntimeException("signal doesnt existe")));
    }

    @Override
    public List<Signal> getAllSignal() {

        return signalRepository.findAll();
    }

    @Override
    public Signal updateSignal(Long id, Signal signal) {
        return null;
    }

    @Override
    public void deleteSignal(Long id) {

        signalRepository.findById(id).orElseThrow();
        signalRepository.deleteById(id);

    }
}
