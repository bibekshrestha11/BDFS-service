package com.bibek.bdfs.knn;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.user.entity.User;

import java.util.List;

public interface KnnService {
    List<User> findNearestDonors(BloodRequestEntity request);
}
