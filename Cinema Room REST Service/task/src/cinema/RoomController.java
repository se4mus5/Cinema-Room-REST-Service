package cinema;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController // makes a class provide exact endpoints (a requested URL) to access the REST methods
public class RoomController {

    private final Room room;

    public RoomController(@Autowired Room room) { // obtains room from Spring IoC container via autowiring
        this.room = room;
    }

    @GetMapping("/seats")
    public Room room() {
        return room;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Map<String, Object>> purchaseSeat(@RequestBody Seat seat) {
        try {
            Seat bookedSeat = room().bookSeat(seat.getRow(), seat.getColumn());
            // customize response format:
            // {
            //    "token": "00ae15f2-1ab6-4a02-a01f-07810b42c0ee",
            //    "ticket": {
            //        "row": 1,
            //        "column": 1,
            //        "price": 10
            //    }
            // }
            return new ResponseEntity<>(Map.of("token", bookedSeat.getToken(), "ticket", bookedSeat), HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new SeatPurchaseException(e.getMessage());
        }
    }

    @PostMapping("/return")
    public ResponseEntity<Map<String, Object>> releaseSeat(@RequestBody String jsonStringWithToken) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getFactory();
            JsonParser jp = factory.createParser(jsonStringWithToken);
            String token = mapper.readTree(jp).get("token").toString().replace("\"",""); // remove double quotes
            Seat releasedSeat = room().unbookSeat(token);
            return new ResponseEntity<>(Map.of("returned_ticket", releasedSeat), HttpStatus.OK);
        } catch (IOException | RuntimeException e) {
            throw new SeatPurchaseException(e.getMessage());
        }
    }

    @ExceptionHandler(SeatPurchaseException.class)
    public ResponseEntity<Map<String, String>> handleSeatPurchaseException(SeatPurchaseException e) {
        // customize response format for exception:
        // {
        //    "error": "... exception message comes here ..."
        // }
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
