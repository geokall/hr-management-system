package service;

import dto.BonusDTO;
import dto.JobInfoDTO;
import entity.HuaBonus;
import entity.HuaUser;
import exception.HuaNotFoundException;
import org.springframework.util.ObjectUtils;
import repository.HuaBonusRepository;
import repository.HuaUserRepository;
import utils.HuaUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class JobJobInfoServiceImpl implements JobInfoService {

    private final HuaUserRepository userRepository;
    private final HuaBonusRepository bonusRepository;

    @Inject
    public JobJobInfoServiceImpl(HuaUserRepository userRepository,
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
                .ifPresentOrElse(huaBonus -> saveBonusBy(dto, huaBonus), () -> {
                    throw new HuaNotFoundException("Bonus not found");
                });
    }

    @Override
    public void deleteBonus(Long id) {
        bonusRepository.findById(id)
                .ifPresentOrElse(bonus -> bonusRepository.deleteById(bonus.getId()), () -> {
                    throw new HuaNotFoundException("Bonus not found");
                });
    }

    @Override
    public JobInfoDTO fetchJobInfo(Long id) {
        HuaUser user = findUser(id);

        JobInfoDTO jobInfoDTO = new JobInfoDTO();

        List<BonusDTO> bonuses = user.getBonuses().stream()
                .map(this::toBonusDTO)
                .collect(Collectors.toList());

        jobInfoDTO.setBonuses(bonuses);

        return jobInfoDTO;
    }


    private BonusDTO toBonusDTO(HuaBonus bonus) {
        BonusDTO bonusDTO = new BonusDTO();
        bonusDTO.setId(bonus.getId());

        if (!ObjectUtils.isEmpty(bonus.getBonusDate())) {
            String bonusDate = HuaUtil.formatDateToString(bonus.getBonusDate());
            bonusDTO.setBonusDate(bonusDate);
        }

        bonusDTO.setAmount(bonus.getAmount());
        bonusDTO.setComment(bonus.getComment());

        return bonusDTO;
    }

    private HuaUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException("User not found"));
    }

    private void saveBonusBy(BonusDTO dto, HuaBonus huaBonus) {
        huaBonus.setComment(dto.getComment());

        Date bonusDate = HuaUtil.formatStringToDate(dto.getBonusDate());

        huaBonus.setBonusDate(bonusDate);
        huaBonus.setAmount(dto.getAmount());

        bonusRepository.save(huaBonus);
    }
}
