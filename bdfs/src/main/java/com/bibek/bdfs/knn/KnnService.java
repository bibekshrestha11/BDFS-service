package com.bibek.bdfs.knn;

import com.bibek.bdfs.blood_request.entity.BloodRequest;
import com.bibek.bdfs.user.entity.User;

import java.util.List;

public interface KnnService {
    List<User> findNearestDonors(BloodRequest request);
}
