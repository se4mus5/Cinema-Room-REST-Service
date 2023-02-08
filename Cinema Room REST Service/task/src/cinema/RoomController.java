package cinema;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController // makes a class provide exact endpoints (a requested URL) to access the REST methods
public class RoomController {

    private final Room room;
    private static final String PASSWORD = "super_secret";

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
            throw new RequestProcessingException(e.getMessage());
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
            throw new RequestProcessingException(e.getMessage());
        }
    }

    @PostMapping("/stats")
    public Stats statistics(@RequestParam String password) {
        try {
            if (password.equals(PASSWORD)) {
                return new Stats(room);
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    @ExceptionHandler(RequestProcessingException.class)
    public ResponseEntity<Map<String, String>> handleRequestProcessingException(RequestProcessingException e) {
        // customize response format for exception:
        // {
        //    "error": "... exception message comes here ..."
        // }
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthenticationException.class,
            MissingServletRequestParameterException.class}) // the only request parameter in this logic is auth-related in this logic
    public ResponseEntity<Map<String, String>> handleAuthenticationException() { // exception needs to match to those declared under @ExceptionHandler
        return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
    }
}
