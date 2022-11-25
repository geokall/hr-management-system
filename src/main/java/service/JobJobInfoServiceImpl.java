package service;

import dto.BonusDTO;
import dto.JobInfoDTO;
import entity.HuaBonus;
import entity.HuaUser;
import exception.HuaNotFoundException;
import repository.HuaBonusRepository;
import repository.HuaUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
    public void updateBonus(Long id, BonusDTO dto) {
        HuaUser user = findUser(id);

        HuaBonus huaBonus = new HuaBonus();
        huaBonus.setUser(user);

        huaBonus.setBonusDate(dto.getBonusDate());
        huaBonus.setAmount(dto.getAmount());
        huaBonus.setComment(dto.getComment());

        bonusRepository.save(huaBonus);
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
        bonusDTO.setBonusDate(bonus.getBonusDate());
        bonusDTO.setAmount(bonus.getAmount());
        bonusDTO.setComment(bonus.getComment());

        return bonusDTO;
    }

    private HuaUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException("User not found"));
    }
}
