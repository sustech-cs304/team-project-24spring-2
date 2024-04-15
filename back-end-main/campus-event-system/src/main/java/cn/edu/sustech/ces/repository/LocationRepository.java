package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface LocationRepository extends CrudRepository<Location, UUID> {
    public Optional<Location> findByName(String name);
    @Query("SELECT l FROM locations l WHERE l.latitude BETWEEN :latitude - :eps AND :latitude + :eps AND l.longitude BETWEEN :longitude - :eps AND :longitude + :eps")
    Optional<Location> findByLatitudeAndLongitude(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("eps") Double eps);
}

