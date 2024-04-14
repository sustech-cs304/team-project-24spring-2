package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Location;
import cn.edu.sustech.ces.repository.LocationRepository;
import cn.edu.sustech.ces.utils.CreationResponse;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public Location getLocationById(UUID id) {
        return locationRepository.findById(id).orElse(null);
    }

    public String createLocation(String name, double latitude, double longitude) {
        Optional<Location> locationOptional = locationRepository.findByLatitudeAndLongitude(latitude, longitude, 0.00001);
        if (locationOptional.isPresent()) {
            CreationResponse response = new CreationResponse(false, "Location already exists with ID: " + locationOptional.get().getId());
            return JSON.toJSONString(response);
        }
        Location location = new Location(name, latitude, longitude);
        location = locationRepository.save(location);
        CreationResponse response = new CreationResponse(true, "Location created with ID: " + location.getId());
        return JSON.toJSONString(response);
    }
}
