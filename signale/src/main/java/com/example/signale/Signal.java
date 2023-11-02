package com.example.signale;

import com.example.signale.Enum.Priority;
import com.example.signale.Enum.SignalType;
import com.example.signale.Enum.StatusSignal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "signal")
public class Signal {

    @Id
    private Long signalId;
    private Date date_Signale;
    private double latitude;
    private double longitude;
    private Long userId;
    private SignalType signalType;
    private Priority priority;
    private StatusSignal status;
    private String description;
    private Long helper_ID;
    private String confirmation;
}
