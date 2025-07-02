package com.bibek.bdfs.donor_matches.service;

import com.bibek.bdfs.donor_matches.dto.DonorMatchResponse;
import com.bibek.bdfs.donor_matches.entity.ResponseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DonorMatchService {
    DonorMatchResponse updateDonorMatchResponse(Long id, ResponseStatus responseStatus);
    Page<DonorMatchResponse> getDonorMatchesByLoginUser(Pageable pageable);
}
