package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Location;
import cn.edu.sustech.ces.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public Location getLocationById(UUID id) {
        return locationRepository.findById(id).orElse(null);
    }
}
