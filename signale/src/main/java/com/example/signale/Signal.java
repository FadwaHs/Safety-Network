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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "signal")
public class Signal {

    @Id
    private Long signalId;
    private LocalDateTime date_Signale;
    private double latitude;
    private double longitude;
    private Long userId;
    private SignalType signalType;
    private Priority priority;
    private StatusSignal status;
    private String description;
    private Long helper_ID;
    private Boolean confirmation;


    public Signal(double latitude, double longitude, Long userId, SignalType signalType, Priority priority, StatusSignal status, String description, Long helper_ID, Boolean confirmation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.signalType = signalType;
        this.priority = priority;
        this.status = status;
        this.description = description;
        this.helper_ID = helper_ID;
        this.confirmation = confirmation;
    }
}
