package gpse.team52.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gpse.team52.contract.RoomService;
import gpse.team52.domain.Equipment;
import gpse.team52.domain.Location;
import gpse.team52.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoomdisplayController {

    @Autowired
    private RoomService roomService;

    public RoomdisplayController(){
        Location location;
        Date date;
        List<Equipment> equipment;
        int start;
        int end;
        int seats;
    }

    /**
     * @return Page with available rooms to choose from
     */
    @GetMapping("/rooms") //TODO request params for meeting, set required = true
    public ModelAndView rooms(
    /*
    @RequestParam(name = "start", required = false) int start,
    @RequestParam(name = "end", required = false) int end,
    @RequestParam(name = "participants", required = false) int seats,
     */
    @RequestParam(name = "location", required = false) Location location,
    @RequestParam(name = "date", required = false) Date date,
    @RequestParam(name = "equipment", required = false) List<Equipment> equipment
    ) {
        final ModelAndView modelAndView = new ModelAndView("rooms");

        // TODO instead of all rooms, get only the ones which are suitable
        // Iterable<Room> roomList = roomService.getAvailableRooms(location, seats, date, start, end,equipment);
        //!! TEST !!
        location = roomService.getLocation("Bielefeld").orElseThrow();
        //Iterable<Room> roomList = roomService.getAllRooms();
        Iterable<Room> roomList = roomService.getAvailableRooms(location, 21, date, 3, 4, equipment);
        //!! TEST !!
        List<Room> rooms = new ArrayList<>(); // is there no other way to check whether an iterable is empty?
        roomList.forEach(rooms::add);
        if(rooms.isEmpty()) {
            modelAndView.addObject("noRoom", true);
        }
        modelAndView.addObject("roomList", roomList);
        return modelAndView;
    }

    /**
     * Details for each room
     * @param roomID room to display details
     * @return Details about specified room
     */
    @GetMapping("/rooms/{roomID}")
    public ModelAndView roomdetails(@PathVariable("roomID") UUID roomID) {
        final ModelAndView modelAndView = new ModelAndView("roomdetails");
        Room room = roomService.getRoom(roomID).orElseThrow(() -> new IllegalArgumentException("No meeting with id: " + roomID + " found"));
        // should add some error handling, if the get method fails

        modelAndView.addObject("room", room);
        List<Equipment> equipment = room.getEquipment();
        modelAndView.addObject("equipmentList", equipment);
        return modelAndView;
    }

    /**
     * Shows page to confirm selected rooms and parameters of meeting
     * @param roomID rooms which are selected
     * @return Page to check and submit data or, in case of error, page with rooms to choose from
     */
    @RequestMapping("/rooms/confirm")
    public ModelAndView confirm(
    @RequestParam(name = "roomID", required = false) String roomID) { // , @RequestParam(name = "meeting", required = true) String meeting
        if (roomID == null) { //return rooms-page with alert message
            final ModelAndView roomsError = new ModelAndView("rooms"); // change to this.rooms( add params of meeting here)
            roomsError.addObject("roomList", roomService.getAllRooms()); // TODO get only available rooms, or change it so that /rooms is used
            roomsError.addObject("error", true);
            return roomsError;
        }
        // else return confirmation page
        final ModelAndView modelAndView = new ModelAndView("confirmbooking");
        String[] chosen = roomID.split(",");
        List<Room> chosenRooms = new ArrayList<Room>();
        for (int i = 0; i < chosen.length; i++) {
            chosenRooms.add(roomService.getRoom(UUID.fromString(chosen[i])).orElseThrow());
        }
        modelAndView.addObject("chosenRooms", chosenRooms);//benötigt, und Meeting auch hinzufügen!
        return modelAndView;
    }
    //TODO success message and return to start page
}
