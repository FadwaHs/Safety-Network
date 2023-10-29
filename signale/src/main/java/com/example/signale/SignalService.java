package com.example.signale;

import java.util.List;
import java.util.Optional;

public interface SignalService {
    Signal addSignal(Signal signal);
    Optional<Signal> getSignal(Long id);
    List<Signal> getAllSignal();
    Signal updateSignal(Long id, Signal signal);
    void deleteSignal(Long id);
}
