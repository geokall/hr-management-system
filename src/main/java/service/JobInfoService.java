package service;

import dto.BonusDTO;
import dto.JobInfoDTO;
import dto.JobInformationDTO;

public interface JobInfoService {

    void createBonus(Long id, BonusDTO dto);

    void updateBonus(Long id, BonusDTO dto);

    void deleteBonus(Long id);

    void updateJobInfo(Long id, JobInformationDTO dto);

    JobInfoDTO fetchJobInformation(Long id);
}
