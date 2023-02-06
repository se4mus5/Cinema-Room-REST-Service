package cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
