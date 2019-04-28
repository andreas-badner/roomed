package gpse.team52.web;

import gpse.team52.Room;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RoomdisplayController {



    private List<Room> roomList = new ArrayList<Room>();
    public RoomdisplayController(){
        // should get a list of rooms to choose for meeting, just set any default
        //Example rooms
        roomList.add(new Room("name", 5, "description", 100));
        roomList.add(new Room("name2", 2, "description2", 101));
    }

    @GetMapping("/rooms")
    public ModelAndView rooms() {
        final ModelAndView modelAndView = new ModelAndView("rooms");
        // Example rooms
        modelAndView.addObject("roomList", roomList);
        return modelAndView;
    }


    @GetMapping("/rooms/{roomID}")
    public ModelAndView roomdetails(@PathVariable("roomID") String roomID) {
        final ModelAndView modelAndView = new ModelAndView("roomdetails");
        Room room = getRoom(roomID);
        modelAndView.addObject("room", room);
        return modelAndView;
    }

    private Room getRoom(String roomID){
        for (Room room : roomList) {
            if (room.getID() == Integer.parseInt(roomID)) {
                return room;
            }
        }
        return null;
    }

}
