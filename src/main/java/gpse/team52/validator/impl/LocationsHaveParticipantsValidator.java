package gpse.team52.validator.impl;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import gpse.team52.form.MeetingCreationForm;
import gpse.team52.validator.LocationsHaveParticipants;

/**
 * Validates that for each location a number of participants exists.
 */
public class LocationsHaveParticipantsValidator implements ConstraintValidator<LocationsHaveParticipants, Object> {
    private boolean hasErrors = false;

    @Override
    public void initialize(final LocationsHaveParticipants constraintAnnotation) {
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        MeetingCreationForm creationForm = (MeetingCreationForm) obj;

        Map<String, Integer> participants = creationForm.getParticipants();

        if (creationForm.getLocations() != null) {
            for (String locationId : creationForm.getLocations()) {
                if (participants == null || !participants.containsKey(locationId)) {
                    addConstraintViolation(context, "participants[" + locationId + "]");
                } else {
                    Integer value = participants.get(locationId);

                    if (value == null || value <= 0) {
                        addConstraintViolation(context, "participants[" + locationId + "]");
                    }
                }
            }
        }

        return !hasErrors;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String node) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
        .addPropertyNode(node).addConstraintViolation();

        hasErrors = true;
    }
}
