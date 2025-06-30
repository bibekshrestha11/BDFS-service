package com.bibek.bdfs.knn.service;

import com.bibek.bdfs.knn.KnnService;
import com.bibek.bdfs.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KnnServiceImpl implements KnnService {
    private final UserInfoRepository userInfoRepository;
}
