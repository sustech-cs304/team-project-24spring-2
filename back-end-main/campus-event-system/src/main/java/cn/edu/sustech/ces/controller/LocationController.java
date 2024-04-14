package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Location;
import cn.edu.sustech.ces.repository.LocationRepository;
import cn.edu.sustech.ces.service.LocationService;
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
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationService locationService;
    @GetMapping("/create")
    public String CreateLocation(
            @RequestParam("name") String name,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {
        return locationService.createLocation(name, latitude, longitude);
    }
}
