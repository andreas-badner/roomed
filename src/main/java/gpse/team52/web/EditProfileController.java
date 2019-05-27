package gpse.team52.web;

import gpse.team52.Command.CreateUserCmd;
import gpse.team52.Convert.Base64EncDec;
import gpse.team52.contract.LocationService;
import gpse.team52.contract.UserService;
import gpse.team52.domain.Location;
import gpse.team52.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

    @Controller
    public class EditProfileController {

        @Autowired
        private UserService userService;
        @Autowired
        private LocationService locationService;

        @GetMapping("/editProfile")
        public ModelAndView editProfile(Authentication authentication) {
            final ModelAndView modelAndView = new ModelAndView("editProfile");

            User user = (User) authentication.getPrincipal();
            modelAndView.addObject("fullName", user.getFirstname() + " " + user.getLastname());
            modelAndView.addObject("email", user.getEmail());

            CreateUserCmd userCmd = new CreateUserCmd();
            userCmd.setFirstname(user.getFirstname());
            userCmd.setLastname(user.getLastname());
            modelAndView.addObject("createUserCmd", userCmd);

            //get All Location and and convert iterable list to an array list of Locations
            ArrayList<Location> locNames = new ArrayList<Location>();
            locationService.getAllLocations().forEach(locNames::add);
            modelAndView.addObject("locationNames",locNames);


            return modelAndView;
        }

        @PostMapping("/editProfile")
        public ModelAndView editProfile(@AuthenticationPrincipal final User user,
                                        @ModelAttribute("createUserCmd") final CreateUserCmd createUserCmd,
                                        @RequestParam("user_profile_default.jpg") MultipartFile file) {

            File pbPicFile = Base64EncDec.convertToFile(file);
            user.setPicture(file.getOriginalFilename());
            user.setFirstname(createUserCmd.getFirstname());
            user.setLastname(createUserCmd.getLastname());
            user.setLocation(createUserCmd.getLocation());
            userService.updateUser(user);
            return new ModelAndView("redirect:/profile");
        }



    }


