package service;

import dto.BonusDTO;
import entity.HuaBonus;
import entity.HuaUser;
import exception.HuaNotFoundException;
import repository.HuaBonusRepository;
import repository.HuaUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class InfoServiceImpl implements InfoService {

    private final HuaUserRepository userRepository;
    private final HuaBonusRepository bonusRepository;

    @Inject
    public InfoServiceImpl(HuaUserRepository userRepository,
                           HuaBonusRepository bonusRepository) {
        this.userRepository = userRepository;
        this.bonusRepository = bonusRepository;
    }

    @Override
    public void updateBonus(Long id, BonusDTO dto) {
        HuaUser user = userRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException("User not found"));

        HuaBonus huaBonus = new HuaBonus();
        huaBonus.setUser(user);

        huaBonus.setBonusDate(dto.getBonusDate());
        huaBonus.setAmount(dto.getAmount());
        huaBonus.setComment(dto.getComment());

        bonusRepository.save(huaBonus);
    }
}
