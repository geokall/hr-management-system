package service;

import dto.BonusDTO;
import dto.JobInformationDTO;

import java.util.List;

public interface JobInfoService {

    void createBonus(Long id, BonusDTO dto);

    void updateBonus(Long id, BonusDTO dto);

    void deleteBonus(Long id);

    void updateJobInfo(Long id, JobInformationDTO dto);

    JobInformationDTO fetchJobInformation(Long id);

    List<BonusDTO> fetchBonus(Long id);
}
