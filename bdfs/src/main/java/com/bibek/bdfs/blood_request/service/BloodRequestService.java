package com.bibek.bdfs.blood_request.service;

import com.bibek.bdfs.blood_request.dto.BloodRequest;
import com.bibek.bdfs.blood_request.dto.BloodResponse;
import com.bibek.bdfs.blood_request.entity.UrgencyLevel;
import com.bibek.bdfs.user.entity.BloodGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BloodRequestService {
    BloodResponse createBloodRequest(BloodRequest bloodRequest);
    BloodResponse getBloodRequestById(Long id);
    BloodResponse updateBloodRequest(Long id, BloodRequest bloodRequest);
    void deleteBloodRequest(Long id);
    Page<BloodResponse> getAllBloodRequests(Pageable pageable);
    Page<BloodResponse> getBloodRequestsByLoggedInUser(Pageable pageable);
    Page<BloodResponse> getBloodRequestsByBloodGroup(BloodGroup bloodGroup, UrgencyLevel urgencyLevel, Pageable pageable);
}
