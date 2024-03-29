package gpse.team52.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import gpse.team52.form.MeetingCreationForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Meeting Entity.
 */
@Entity
@NoArgsConstructor
public class Meeting {
    /**
     * Unique id for each individual meeting.
     */
    @Id
    @Getter
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false,
    columnDefinition = "BINARY(16)")
    private UUID meetingId;

    /**
     * Owner of the meeting.
     */
    @Getter
    @Setter
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId")
    private User owner;

    /**
     * List of participants for the meeting.
     */
    @Getter
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();

    /**
     * Date and time for the beginning of the meeting.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDateTime startAt;

    /**
     * Date and time for the ending of the meeting.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDateTime endAt;

    /**
     * Title of the meeting.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private String title;

    /**
     * Boolean if room is confirmed, otherwise it will be canceled.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private boolean confirmed = false;

    /**
     * Description of the meeting.
     */
    @Getter
    @Setter
    @Column
    private String description;

    /**
     * Boolean if a confirmation email was already send.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private boolean confirmemail = false;

    /**
     * The rooms where meetings are held.
     */
    @Getter
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private Set<MeetingRoom> rooms = new HashSet<>();

    /**
     * Boolean if meeting room can be changed.
     * Can only be changed by specified users. (Admin etc)
     */
    @Getter
    @Setter
    @Column (nullable = false)
    private boolean disableRebookMeeting = false;

    /**
     * Constructor for Meeting with parameters.
     *
     * @param title Title of the meeting
     */
    public Meeting(final String title) {
        this.title = title;
    }

    /**
     * Calculates duration of the meeting.
     *
     * @return Duration as int.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public int getDuration() {
        return (int) (Duration.between(startAt, endAt).getSeconds() / 60);
    }

    /**
     * Add a new participant to a meeting.
     *
     * @param participant The participant that is added
     */
    public void addParticipant(final Participant participant) {
        participants.add(participant);
        participant.setMeeting(this);
    }

    /**
     * Add a meeting room to this meeting.
     *
     * @param meetingRoom The Room to be added.
     */
    public void addRoom(final MeetingRoom meetingRoom) {
        rooms.add(meetingRoom);
        meetingRoom.setMeeting(this);
    }

    /**
     * Remove a meeting room from this meeting.
     *
     * @param meetingRoom The Room to be deleted.
     */
    public void removeRoom(final MeetingRoom meetingRoom) {
        rooms.remove(meetingRoom);
    }

    /**
     * Return the total number of participants.
     *
     * @return Number of participants.
     */
    public int getParticipantsNumber() {
        return rooms.stream().mapToInt(item -> item.getParticipants()).sum();
    }

    /**
     * Get startAt with timezone offset.
     *
     * @param offset Offset.
     * @return LocalDateTime.
     */
    public LocalDateTime getStartAt(final long offset) {
        return getStartAt().plusMinutes(offset);
    }

    /**
     * get start at time.
     * @param user User.
     * @return LocalDateTime.
     */
    public LocalDateTime getStartAt(final User user) {
        if (user.getLocation() != null) {
            return getStartAt(user.getLocation().getTimeoffset());
        }

        return getStartAt();
    }

    /**
     * Get endAt with timezone offset.
     * @param offset Offset.
     * @return LocalDateTime.
     */
    public LocalDateTime getEndAt(final long offset) {
        return getEndAt().plusMinutes(offset);
    }

    /**
     * get endAt with timezone offset.
     * @param user User offset.
     * @return LocalDateTime.
     */
    public LocalDateTime getEndAt(final User user) {
        if (user.getLocation() != null) {
            return getEndAt(user.getLocation().getTimeoffset());
        }

        return getEndAt();
    }

    /**
     * MeetingToString().
     * @return Meeting as String.
     */
    public String meetingToString() {

        //title;startdate;enddate;participants;owner;confirmed;description
        String participant = ""; //NOPMD
        if (!participants.isEmpty()) {
            participant = participants.get(0).getEmail() + "_" + participants.get(0).getFirstName() + "_" //NOPMD
            + participants.get(0).getLastName(); //NOPMD
            for (int i = 1; i < participants.size(); i++) {
                participant = participant + "," + participants.get(i).getEmail() + "_" //NOPMD
                + participants.get(i).getFirstName() + "_" + participants.get(i).getLastName(); //NOPMD
            }
        }
        final String start = startAt.toString().replace("T", " ");
        final String end = endAt.toString().replace("T", " ");

        final String string = title + ";" + start + ";" + end + ";" + participant + ";" + owner.getEmail() + ";"
        + confirmed + ";" + description;
        return string; //NOPMD
    }

    /**
     * Convert a Meeting entity to meeting creation form.
     * @return MeetingCreationForm.
     */
    public MeetingCreationForm toMeetingCreationForm() { //NOPMD
        final MeetingCreationForm meetingCreationForm = new MeetingCreationForm();
        meetingCreationForm.setStartDate(getStartAt().toLocalDate());
        meetingCreationForm.setStartTime(getStartAt().toLocalTime());
        meetingCreationForm.setEndDate(getEndAt().toLocalDate());
        meetingCreationForm.setEndTime(getEndAt().toLocalTime());
        meetingCreationForm.setName(getTitle());

        final List<String> locations = new ArrayList<>();
        final Map<String, Integer> participants = new HashMap<>();
        final Map<String, List<String>> equipment = new HashMap<>();

        for (final MeetingRoom meetingRoom : getRooms()) {
            if (!locations.contains(meetingRoom.getRoom().getLocation().getLocationId().toString())) {
                locations.add(meetingRoom.getRoom().getLocation().getLocationId().toString());
                participants.put(
                    meetingRoom.getRoom().getLocation().getLocationId().toString(),
                    meetingRoom.getParticipants()
                );
                final List<String> equipmentList = new ArrayList<>(); //NOPMD
                for (final Equipment item : meetingRoom.getRoom().getEquipment()) {
                    equipmentList.add(item.getEquipmentID().toString());
                }

                equipment.put(meetingRoom.getRoom().getLocation().toString(), equipmentList);
            }
        }

        meetingCreationForm.setLocations(locations);
        meetingCreationForm.setParticipants(participants);
        meetingCreationForm.setEquipment(equipment);

        return meetingCreationForm;
    }
}
