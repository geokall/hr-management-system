package service;

import dto.BonusDTO;
import dto.EducationDTO;
import dto.JobInformationDTO;
import entity.HuaBonus;
import entity.HuaEducation;
import entity.HuaUser;
import enums.EthnicityEnum;
import enums.JobCategoryEnum;
import exception.HuaNotFoundException;
import repository.HuaBonusRepository;
import repository.HuaEducationRepository;
import repository.HuaUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static exception.HuaCommonError.*;
import static utils.HuaUtil.formatDateToString;
import static utils.HuaUtil.formatStringToDate;

@ApplicationScoped
public class JobInfoServiceImpl implements JobInfoService {

    private final HuaUserRepository userRepository;
    private final HuaBonusRepository bonusRepository;
    private final HuaEducationRepository educationRepository;

    @Inject
    public JobInfoServiceImpl(HuaUserRepository userRepository,
                              HuaBonusRepository bonusRepository,
                              HuaEducationRepository educationRepository) {
        this.userRepository = userRepository;
        this.bonusRepository = bonusRepository;
        this.educationRepository = educationRepository;
    }

    @Override
    public void createBonus(Long id, BonusDTO dto) {
        HuaUser user = findUser(id);

        HuaBonus huaBonus = new HuaBonus();
        huaBonus.setUser(user);

        saveBonusBy(dto, huaBonus);

        bonusRepository.save(huaBonus);
    }

    @Override
    public void updateBonus(Long id, BonusDTO dto) {
        bonusRepository.findById(id)
                .ifPresentOrElse(huaBonus -> saveBonusBy(dto, huaBonus),
                        () -> {
                            throw new HuaNotFoundException(BONUS_NOT_FOUND);
                        });
    }

    @Override
    public void deleteBonus(Long id) {
        bonusRepository.findById(id)
                .ifPresentOrElse(bonus -> bonusRepository.deleteById(bonus.getId()),
                        () -> {
                            throw new HuaNotFoundException(BONUS_NOT_FOUND);
                        });
    }

    @Override
    public void updateJobInfo(Long id, JobInformationDTO dto) {
        HuaUser user = findUser(id);

        user.setHireDate(formatStringToDate(dto.getHireDate()));
        user.setEthnicity(EthnicityEnum.valueOf(dto.getEthnicity()));
        user.setJobCategory(JobCategoryEnum.valueOf(dto.getJobCategory()));
        user.setJobDescription(dto.getJobDescription());

        userRepository.save(user);
    }

    @Override
    public JobInformationDTO fetchJobInformation(Long id) {
        HuaUser user = findUser(id);

        JobInformationDTO jobInfoDTO = new JobInformationDTO();

        jobInfoDTO.setEthnicity(user.getEthnicity() != null ? user.getEthnicity().name() : null);
        jobInfoDTO.setJobCategory(user.getJobCategory() != null ? user.getJobCategory().name() : null);

        jobInfoDTO.setHireDate(formatDateToString(user.getHireDate()));

        jobInfoDTO.setJobDescription(user.getJobDescription());

        List<BonusDTO> bonuses = user.getBonuses().stream()
                .map(this::toBonusDTO)
                .collect(Collectors.toList());

        jobInfoDTO.setBonuses(bonuses);

        return jobInfoDTO;
    }

    @Override
    public void createEducation(Long id, EducationDTO dto) {
        HuaUser user = findUser(id);

        HuaEducation education = new HuaEducation();
        education.setUser(user);

        saveEducationBy(dto, education);

        educationRepository.save(education);
    }

    @Override
    public void updateEducation(Long id, EducationDTO dto) {
        educationRepository.findById(id)
                .ifPresentOrElse(education -> saveEducationBy(dto, education),
                        () -> {
                            throw new HuaNotFoundException(EDUCATION_NOT_FOUND);
                        });
    }

    @Override
    public void deleteEducation(Long id) {
        educationRepository.findById(id)
                .ifPresentOrElse(education -> educationRepository.deleteById(education.getId()),
                        () -> {
                            throw new HuaNotFoundException(BONUS_NOT_FOUND);
                        });
    }


    private BonusDTO toBonusDTO(HuaBonus bonus) {
        BonusDTO bonusDTO = new BonusDTO();
        bonusDTO.setId(bonus.getId());

        String bonusDate = formatDateToString(bonus.getBonusDate());
        bonusDTO.setBonusDate(bonusDate);

        bonusDTO.setAmount(bonus.getAmount());
        bonusDTO.setComment(bonus.getComment());

        return bonusDTO;
    }

    private HuaUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    throw new HuaNotFoundException(USER_NOT_FOUND);
                });
    }

    private void saveBonusBy(BonusDTO dto, HuaBonus huaBonus) {
        huaBonus.setComment(dto.getComment());

        Date bonusDate = formatStringToDate(dto.getBonusDate());

        huaBonus.setBonusDate(bonusDate);
        huaBonus.setAmount(dto.getAmount());

        bonusRepository.save(huaBonus);
    }

    private void saveEducationBy(EducationDTO dto, HuaEducation education) {
        education.setCollege(dto.getCollege());
        education.setGpa(dto.getGpa());
        education.setDegree(dto.getDegree());
        education.setSpecialization(dto.getSpecialization());

        Date studyFrom = formatStringToDate(dto.getStudyFrom());
        Date studyTo = formatStringToDate(dto.getStudyTo());

        education.setStudyFrom(studyFrom);
        education.setStudyTo(studyTo);
    }

}
