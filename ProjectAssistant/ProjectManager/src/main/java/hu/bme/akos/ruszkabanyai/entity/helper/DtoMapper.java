package hu.bme.akos.ruszkabanyai.entity.helper;

import hu.bme.akos.ruszkabanyai.dao.interfaces.ProjectRepository;
import hu.bme.akos.ruszkabanyai.dao.interfaces.UserRepository;
import hu.bme.akos.ruszkabanyai.dto.MeetingDTO;
import hu.bme.akos.ruszkabanyai.entity.Meeting;
import org.springframework.beans.factory.annotation.Autowired;

public class DtoMapper {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public static Meeting dtoToEntity(final MeetingDTO dto) {

        return null;
    }

}
