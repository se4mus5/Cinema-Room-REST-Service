package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Stats {
    Room room;

    @Autowired
    @JsonIgnore
    public Stats(Room room) {
        this.room = room;
    }

    @JsonProperty
    public int currentIncome() {
        return room.currentIncome();
    }

    @JsonProperty
    public int numberOfAvailableSeats() {
        return room.numberOfAvailableSeats();
    }

    @JsonProperty
    public int numberOfPurchasedTickets() {
        return room.numberOfPurchasedTickets();
    }

}
