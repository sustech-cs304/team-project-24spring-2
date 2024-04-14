package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Location;
import cn.edu.sustech.ces.repository.LocationRepository;
import cn.edu.sustech.ces.service.UserService;
import cn.edu.sustech.ces.utils.CreationResponse;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;
    @GetMapping("/test-create-location")
    public String testCreateLocation(
            @RequestParam("name") String name,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {

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
