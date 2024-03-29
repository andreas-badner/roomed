package gpse.team52.seeder;

import javax.annotation.PostConstruct;

import gpse.team52.contract.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Initializes the default locations.
 */
@Service
@RequiredArgsConstructor
public class InitializeDefaultLocations {

    private final LocationService locationService;

    /**
     * Initializes locations to use them in DefaultMeetings and DefaultRooms.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    @PostConstruct
    public void init() {

        locationService.createLocation("Bielefeld", 120);
        locationService.createLocation("Gütersloh", 120);
        locationService.createLocation("Düsseldorf", 120);
        locationService.createLocation("Ratingen", 120);
        locationService.createLocation("Mumbai", 210);

    }
}
