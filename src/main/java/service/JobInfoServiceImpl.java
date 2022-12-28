package service;

import dto.*;
import entity.HuaBonus;
import entity.HuaCompensation;
import entity.HuaUser;
import entity.HuaWorkInformation;
import enums.EthnicityEnum;
import enums.JobCategoryEnum;
import enums.PayTypeEnum;
import exception.HuaNotFoundException;
import repository.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static exception.HuaCommonError.*;
import static utils.HuaUtil.formatDateToString;
import static utils.HuaUtil.formatStringToDate;

@ApplicationScoped
public class JobInfoServiceImpl implements JobInfoService {

    private final HuaUserRepository userRepository;
    private final HuaBonusRepository bonusRepository;
    private final HuaLocationRepository locationRepository;
    private final HuaDivisionRepository divisionRepository;
    private final HuaWorkInformationRepository workInformationRepository;
    private final HuaCompensationRepository compensationRepository;

    @Inject
    public JobInfoServiceImpl(HuaUserRepository userRepository,
                              HuaBonusRepository bonusRepository,
                              HuaLocationRepository locationRepository,
                              HuaDivisionRepository divisionRepository,
                              HuaWorkInformationRepository workInformationRepository,
                              HuaCompensationRepository compensationRepository) {
        this.userRepository = userRepository;
        this.bonusRepository = bonusRepository;
        this.locationRepository = locationRepository;
        this.divisionRepository = divisionRepository;
        this.workInformationRepository = workInformationRepository;
        this.compensationRepository = compensationRepository;
    }

    @Override
    public void createBonus(Long id, BonusDTO dto) {
        HuaUser user = findUser(id);

        HuaBonus huaBonus = new HuaBonus();
        huaBonus.setUser(user);

        saveBonusBy(dto, huaBonus);
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

        List<WorkInformationDTO> listOfWorkInformations = workInformationRepository.findByUserId(user.getId()).stream()
                .map(this::toWorkInformationDTO)
                .collect(Collectors.toList());

        jobInfoDTO.setWorkInformations(listOfWorkInformations);

        List<CompensationDTO> compensations = compensationRepository.findByUserId(user.getId()).stream()
                .map(this::toCompensationDTO)
                .collect(Collectors.toList());

        jobInfoDTO.setCompensations(compensations);

        return jobInfoDTO;
    }

    @Override
    public List<BonusDTO> fetchBonus(Long id) {
        HuaUser user = findUser(id);

        return bonusRepository.findByUser(user).stream()
                .map(this::toBonusDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkInformationDTO> fetchWorkInformation(Long id) {
        return workInformationRepository.findByUserId(id).stream()
                .map(this::toWorkInformationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void createWorkInformation(Long id, WorkInformationDTO dto) {
        HuaUser user = findUser(id);

        HuaWorkInformation workInformation = new HuaWorkInformation();
        workInformation.setUser(user);

        saveWorkInformationBy(dto, workInformation);
    }

    @Override
    public void updateWorkInformation(Long id, WorkInformationDTO dto) {
        workInformationRepository.findById(id)
                .ifPresentOrElse(workInformation -> saveWorkInformationBy(dto, workInformation),
                        () -> {
                            throw new HuaNotFoundException(WORK_INFORMATION_NOT_FOUND);
                        });
    }

    @Override
    public void deleteWorkInformation(Long id) {
        workInformationRepository.findById(id)
                .ifPresentOrElse(workInformation ->
                                workInformationRepository.deleteById(workInformation.getId()),
                        () -> {
                            throw new HuaNotFoundException(WORK_INFORMATION_NOT_FOUND);
                        });
    }

    @Override
    public void createCompensation(Long id, CompensationDTO dto) {
        HuaUser user = findUser(id);

        HuaCompensation compensation = new HuaCompensation();
        compensation.setUser(user);

        saveCompensationBy(dto, compensation);
    }

    @Override
    public void updateCompensation(Long id, CompensationDTO dto) {
        compensationRepository.findById(id)
                .ifPresentOrElse(compensation -> saveCompensationBy(dto, compensation),
                        () -> {
                            throw new HuaNotFoundException(COMPENSATION_NOT_FOUND);
                        });
    }

    @Override
    public void deleteCompensation(Long id) {
        compensationRepository.findById(id)
                .ifPresentOrElse(compensation ->
                                compensationRepository.deleteById(compensation.getId()),
                        () -> {
                            throw new HuaNotFoundException(COMPENSATION_NOT_FOUND);
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

    private WorkInformationDTO toWorkInformationDTO(HuaWorkInformation workInformation) {
        WorkInformationDTO dto = new WorkInformationDTO();
        dto.setId(workInformation.getId());
        dto.setJobTitle(workInformation.getJobTitle());
        dto.setEffectiveDate(formatDateToString(workInformation.getEffectiveDate()));

        IdNameDTO locationDTO = Optional.ofNullable(workInformation.getLocation())
                .map(location -> new IdNameDTO(location.getId(), location.getName()))
                .orElse(null);

        dto.setLocation(locationDTO);

        IdNameDTO divisionDTO = Optional.ofNullable(workInformation.getDivision())
                .map(division -> new IdNameDTO(division.getId(), division.getName()))
                .orElse(null);

        dto.setDivision(divisionDTO);

        IdNameDTO managerDTO = Optional.ofNullable(workInformation.getManager())
                .map(manager -> new IdNameDTO(manager.getId(), manager.getName()))
                .orElse(null);

        dto.setManager(managerDTO);

        return dto;
    }

    private void saveWorkInformationBy(WorkInformationDTO dto, HuaWorkInformation workInformation) {
        Date effectiveDate = formatStringToDate(dto.getEffectiveDate());
        workInformation.setEffectiveDate(effectiveDate);

        workInformation.setJobTitle(dto.getJobTitle());

        Optional.ofNullable(dto.getLocation())
                .map(IdNameDTO::getId)
                .flatMap(locationRepository::findById)
                .ifPresent(workInformation::setLocation);

        Optional.ofNullable(dto.getDivision())
                .map(IdNameDTO::getId)
                .flatMap(divisionRepository::findById)
                .ifPresent(workInformation::setDivision);

        Optional.ofNullable(dto.getManager())
                .map(IdNameDTO::getId)
                .flatMap(userRepository::findById)
                .ifPresent(workInformation::setManager);

        workInformationRepository.save(workInformation);
    }

    private void saveCompensationBy(CompensationDTO dto, HuaCompensation compensation) {
        Date effectiveDate = formatStringToDate(dto.getEffectiveDate());

        compensation.setEffectiveDate(effectiveDate);
        compensation.setPayType(PayTypeEnum.valueOf(dto.getPayType()));
        compensation.setPayRate(dto.getPayRate());
        compensation.setComment(dto.getComment());
    }

    private CompensationDTO toCompensationDTO(HuaCompensation huaCompensation) {
        CompensationDTO compensation = new CompensationDTO();
        compensation.setId(huaCompensation.getId());
        compensation.setEffectiveDate(formatDateToString(huaCompensation.getEffectiveDate()));
        compensation.setPayType(huaCompensation.getPayType() != null ? huaCompensation.getPayType().name() : null);
        compensation.setPayRate(huaCompensation.getPayRate());
        compensation.setComment(huaCompensation.getComment());

        return compensation;
    }

}
