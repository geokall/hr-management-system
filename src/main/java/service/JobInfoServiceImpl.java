package service;

import dto.BonusDTO;
import dto.JobInfoDTO;
import dto.JobInformationDTO;
import entity.HuaBonus;
import entity.HuaUser;
import enums.EthnicityEnum;
import enums.JobCategoryEnum;
import repository.HuaBonusRepository;
import repository.HuaUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static exception.HuaCommonError.BONUS_NOT_FOUND;
import static exception.HuaCommonError.USER_NOT_FOUND;
import static utils.HuaUtil.*;

@ApplicationScoped
public class JobInfoServiceImpl implements JobInfoService {

    private final HuaUserRepository userRepository;
    private final HuaBonusRepository bonusRepository;

    @Inject
    public JobInfoServiceImpl(HuaUserRepository userRepository,
                              HuaBonusRepository bonusRepository) {
        this.userRepository = userRepository;
        this.bonusRepository = bonusRepository;
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
                        () -> throwNotFoundExceptionBy(BONUS_NOT_FOUND));
    }

    @Override
    public void deleteBonus(Long id) {
        bonusRepository.findById(id)
                .ifPresentOrElse(bonus -> bonusRepository.deleteById(bonus.getId()),
                        () -> throwNotFoundExceptionBy(BONUS_NOT_FOUND));
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
    public JobInfoDTO fetchJobInformation(Long id) {
        HuaUser user = findUser(id);

        JobInfoDTO jobInfoDTO = new JobInfoDTO();

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
                .orElseThrow(() -> throwNotFoundExceptionBy(USER_NOT_FOUND));
    }

    private void saveBonusBy(BonusDTO dto, HuaBonus huaBonus) {
        huaBonus.setComment(dto.getComment());

        Date bonusDate = formatStringToDate(dto.getBonusDate());

        huaBonus.setBonusDate(bonusDate);
        huaBonus.setAmount(dto.getAmount());

        bonusRepository.save(huaBonus);
    }

}
