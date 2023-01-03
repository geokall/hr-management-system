package service;

import dto.*;

import java.util.List;

public interface JobInfoService {

    void createBonus(Long id, BonusDTO dto);

    void updateBonus(Long id, BonusDTO dto);

    void deleteBonus(Long id);

    void updateJobInfo(Long id, JobInformationDTO dto);

    JobInformationDTO fetchJobInformation(Long id);

    IdNameDTO fetchLastEffectiveWorkInfo(Long id);

    List<BonusDTO> fetchBonus(Long id);

    List<WorkInformationDTO> fetchWorkInformations(Long id);

    List<CompensationDTO> fetchCompensations(Long id);

    void createWorkInformation(Long id, WorkInformationDTO dto);

    void updateWorkInformation(Long id, WorkInformationDTO dto);

    void deleteWorkInformation(Long id);

    void createCompensation(Long id, CompensationDTO dto);

    void updateCompensation(Long id, CompensationDTO dto);

    void deleteCompensation(Long id);

}
