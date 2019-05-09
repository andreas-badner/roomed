package gpse.team52.repository;

import gpse.team52.domain.Participant;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ParticipantRepository extends CrudRepository<Participant, UUID> {
}
