package cinema;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // renames http parameters: camelCase variable -> snake_case http param, alternative @JsonProperty("some_text") or application.properties parameter
public class Room {
    private final int totalRows;
    private final int totalColumns;
    private final List<Seat> availableSeats;

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

    public int getTotalColumns() {
        return totalColumns;
    }

    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }

    public Seat bookSeat(int row, int column) {
        if (row < 1 || column < 1 || row > totalRows || column > totalRows) {
            throw new IndexOutOfBoundsException("The number of a row or a column is out of bounds!");
        }

        for (Seat s : availableSeats) {
            if (s.getRow() == row && s.getColumn() == column) {
                if (s.isAvailable()) {
                    s.setAvailable(false);
                    s.setToken(UUID.randomUUID().toString());
                    return s;
                } else {
                    throw new RuntimeException("The ticket has been already purchased!");
                }
            }
        }

        throw new RuntimeException("Unexpected server-side error."); // if we get here, data is very likely invalid
    }

    public Seat unbookSeat(String token) {
        for (Seat s : availableSeats) {
            if (s.getToken().equals(token)) { // useful breakpoint for debugging
                s.setAvailable(true);
                s.setToken(""); // clear token after refund transaction
                return s;
            }
        }

        throw new RuntimeException("Wrong token!");
    }

    public int currentIncome() {
        return availableSeats.stream()
                .filter(s -> !s.isAvailable())
                .mapToInt(s -> s.getPrice())
                .sum();
    }

    public int numberOfAvailableSeats() {
        return Math.toIntExact(availableSeats.stream()
                .filter(s -> s.isAvailable())
                .count());
    }

    public int numberOfPurchasedTickets() {
        return Math.toIntExact(availableSeats.stream()
                .filter(s -> !s.isAvailable())
                .count());
    }
}
