package com.bibek.bdfs.knn.service;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.knn.KnnService;
import com.bibek.bdfs.user.entity.BloodGroup;
import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KnnServiceImpl implements KnnService {

    private final UserRepository userRepository;

    private static final Map<BloodGroup, List<BloodGroup>> COMPATIBLE_BLOOD_TYPES = new HashMap<>();

    static {
        COMPATIBLE_BLOOD_TYPES.put(BloodGroup.A_POSITIVE, Arrays.asList(BloodGroup.A_POSITIVE, BloodGroup.A_NEGATIVE, BloodGroup.O_POSITIVE, BloodGroup.O_NEGATIVE));
        COMPATIBLE_BLOOD_TYPES.put(BloodGroup.A_NEGATIVE, Arrays.asList(BloodGroup.A_NEGATIVE, BloodGroup.O_NEGATIVE));
        COMPATIBLE_BLOOD_TYPES.put(BloodGroup.B_POSITIVE, Arrays.asList(BloodGroup.B_POSITIVE, BloodGroup.B_NEGATIVE, BloodGroup.O_POSITIVE, BloodGroup.O_NEGATIVE));
        COMPATIBLE_BLOOD_TYPES.put(BloodGroup.B_NEGATIVE, Arrays.asList(BloodGroup.B_NEGATIVE, BloodGroup.O_NEGATIVE));
        COMPATIBLE_BLOOD_TYPES.put(BloodGroup.AB_POSITIVE, Arrays.asList(BloodGroup.A_POSITIVE, BloodGroup.A_NEGATIVE, BloodGroup.B_POSITIVE, BloodGroup.B_NEGATIVE, BloodGroup.AB_POSITIVE, BloodGroup.AB_NEGATIVE, BloodGroup.O_POSITIVE, BloodGroup.O_NEGATIVE));
        COMPATIBLE_BLOOD_TYPES.put(BloodGroup.AB_NEGATIVE, Arrays.asList(BloodGroup.A_NEGATIVE, BloodGroup.B_NEGATIVE, BloodGroup.AB_NEGATIVE, BloodGroup.O_NEGATIVE));
        COMPATIBLE_BLOOD_TYPES.put(BloodGroup.O_POSITIVE, Arrays.asList(BloodGroup.O_POSITIVE, BloodGroup.O_NEGATIVE));
        COMPATIBLE_BLOOD_TYPES.put(BloodGroup.O_NEGATIVE, Collections.singletonList(BloodGroup.O_NEGATIVE));
    }

    @Override
    public List<User> findNearestDonors(BloodRequestEntity request) {
        BloodGroup requestedBloodGroup = request.getBloodTypeNeeded();  // enum type
        List<BloodGroup> compatibleGroups = COMPATIBLE_BLOOD_TYPES.getOrDefault(requestedBloodGroup, new ArrayList<>());

        if (compatibleGroups.isEmpty()) {
            log.warn("No compatible blood groups found for {}", requestedBloodGroup);
            return Collections.emptyList();
        }

        // Fetch all eligible donors
        List<User> eligibleDonors = userRepository.findByBloodGroupInAndIsActiveTrue(compatibleGroups);
        log.info("Fetched {} eligible donors for blood groups: {}", eligibleDonors.size(), compatibleGroups);

        double reqLat = request.getLatitude();
        double reqLon = request.getLongitude();

        double radius = 10.0;
        final double MAX_RADIUS = 25.0;
        final double STEP = 0.5;

        while (radius <= MAX_RADIUS) {
            double finalRadius = radius;
            List<User> nearbyDonors = eligibleDonors.stream()
                    .filter(user -> {
                        double distance = distanceInKm(reqLat, reqLon, user.getLatitude(), user.getLongitude());
                        log.debug("User: {}, Distance: {} km, Radius: {} km", user.getEmailId(), distance, finalRadius);
                        return distance <= finalRadius;
                    })
                    .toList();

            if (!nearbyDonors.isEmpty()) {
                log.info("Found {} nearby donors within {} km radius", nearbyDonors.size(), finalRadius);
                return nearbyDonors;
            }

            radius += STEP;
        }

        log.info("No donors found within {} km radius", MAX_RADIUS);
        return Collections.emptyList(); // No match found within 25km
    }

    private double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;
        log.debug("Distance between ({}, {}) and ({}, {}) is {} km", lat1, lon1, lat2, lon2, distance);
        return distance;
    }


}