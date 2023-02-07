package cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController // makes a class provide exact endpoints (a requested URL) to access the REST methods
public class RoomController {

    private final Room room;

    public RoomController(@Autowired Room room) { // obtains room from Spring IoC container via autowiring
        this.room = room;
    }

    @GetMapping("/seats")
    public Room returnRoom() {
        return room;
    }

    @PostMapping("/purchase")
    public Seat purchaseSeat(@RequestBody Seat seat) {
        try {
            return returnRoom().bookSeat(seat.getRow(), seat.getColumn());
        } catch (RuntimeException e) {
            throw new SeatPurchaseException(e.getMessage());
        }
    }

    @ExceptionHandler(SeatPurchaseException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementFoundException(SeatPurchaseException e) {
        // customize response format for exception:
        // {
        //    "error": "... exception message comes here ..."
        // }
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
