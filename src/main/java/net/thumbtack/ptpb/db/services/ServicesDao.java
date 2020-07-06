package net.thumbtack.ptpb.db.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ServicesDao {
    private final ServicesMapper servicesMapper;

    public void insertServices(Services todoist) {
        servicesMapper.save(todoist);
    }

    public Optional<Services> getServicesByUserUuid(String userUuid) {
        return servicesMapper.findById(userUuid);
    }

    public void delete(String userUuid) {
        servicesMapper.deleteById(userUuid);
    }

    public void deleteAllServices() {
        servicesMapper.deleteAll();
    }
}
