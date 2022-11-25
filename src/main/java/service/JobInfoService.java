package service;

import dto.BonusDTO;
import dto.JobInfoDTO;

public interface JobInfoService {

    void updateBonus(Long id, BonusDTO dto);

    JobInfoDTO fetchJobInfo(Long id);
}
