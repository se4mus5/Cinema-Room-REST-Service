package cinema;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // renames http parameters: camelCase variable -> snake_case http param, alternative @JsonProperty("some_text") or application.properties parameter
public class Room {
    private int totalRows;
    private int totalColumns;
    private List<Seat> availableSeats;

//    public Room(int totalRows, int totalColumns, List<Seat> availableSeats) {
//        this.totalRows = totalRows;
//        this.totalColumns = totalColumns;
//        this.availableSeats = availableSeats;
//    }

    public Room() {
        totalRows = 9;
        totalColumns = 9;
        availableSeats = new ArrayList<>();
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalRows; j++) {
                availableSeats.add(new Seat(i + 1, j + 1));
            }
        }
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(int totalColumns) {
        this.totalColumns = totalColumns;
    }

    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(List<Seat> availableSeats) {
        this.availableSeats = availableSeats;
    }
}
