package com.ridelink.ridemanagement.repository;

import com.ridelink.ridemanagement.domain.Ride;
import com.ridelink.ridemanagement.domain.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RideRepository extends JpaRepository<Ride, UUID> {

    List<Ride> findByPassengerId(UUID passengerId);

    List<Ride> findByDriverId(UUID driverId);

    List<Ride> findByStatus(RideStatus status);
}
