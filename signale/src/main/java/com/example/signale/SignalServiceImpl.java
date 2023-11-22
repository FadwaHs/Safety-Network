package com.example.signale;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Service
@AllArgsConstructor
public class SignalServiceImpl implements SignalService{

    private  SignalRepository signalRepository ;

    public Long findMaxId(){
        List<Signal> maxSignal = signalRepository.findMaxSignalId();
        if(maxSignal.isEmpty()){
            return 0L;
        }
        return  maxSignal.get(0).getSignalId();
    }


    @Override
    public Signal addSignal(Signal signal) {
        signal.setSignalId(findMaxId()+1);
        signal.setDate_Signale(LocalDateTime.now());
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

       Signal signal1 = getSignal(id).get();
       if(signal1 != null){

           if(signal1.getConfirmation()){
               throw new RuntimeException("Cant update this signal");
           }

           if (!"".equalsIgnoreCase(signal.getConfirmation().toString()))
               signal1.setConfirmation(signal.getConfirmation());

           if (!"".equalsIgnoreCase(signal.getSignalType().toString()))
               signal1.setSignalType(signal.getSignalType());

           if (!"".equalsIgnoreCase(signal.getPriority().toString()))
               signal1.setPriority(signal.getPriority());

           if (!"".equalsIgnoreCase(signal.getStatus().toString()))
               signal1.setStatus(signal.getStatus());

           if (!"".equalsIgnoreCase(signal.getDescription()))
               signal1.setDescription(signal.getDescription());

           if (!"".equalsIgnoreCase(signal.getAdresse()))
               signal1.setAdresse(signal.getAdresse());

            if(signal.getHelper_ID() !=null){
                if (!"".equalsIgnoreCase(signal.getHelper_ID().toString()) )
                    signal1.setHelper_ID(signal.getHelper_ID());
            }




            return signalRepository.save(signal1);
       }

        return null;
    }

    @Override
    public void deleteSignal(Long id) {

        signalRepository.findById(id).orElseThrow(() -> new RuntimeException("Impossible de suprimer"));
        signalRepository.deleteById(id);

    }
}
