package service;

import dto.BonusDTO;
import dto.JobInfoDTO;

public interface JobInfoService {

    void createBonus(Long id, BonusDTO dto);

    void updateBonus(Long id, BonusDTO dto);

    void deleteBonus(Long id);

    JobInfoDTO fetchJobInfo(Long id);
}
