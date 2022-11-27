package service;

import dto.InformationDTO;
import dto.PersonalInformationDTO;
import entity.HuaUser;
import enums.EmployeeStatusEnum;
import enums.GenderEnum;
import enums.MaritalStatusEnum;
import exception.HuaNotFoundException;
import repository.HuaUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Date;

import static utils.HuaUtil.formatDateToString;
import static utils.HuaUtil.formatStringToDate;

@ApplicationScoped
public class PersonalInfoServiceImpl implements PersonalInfoService {

    private final HuaUserRepository huaUserRepository;

    @Inject
    public PersonalInfoServiceImpl(HuaUserRepository huaUserRepository) {
        this.huaUserRepository = huaUserRepository;
    }

    @Override
    public InformationDTO findPersonalInfo(Long id) {
        HuaUser user = findUserBy(id);

        return toUserDTO(user);
    }

    @Override
    public void updateBasicInformation(Long id, InformationDTO dto) {
        HuaUser user = findUserBy(id);

        updateBasicInformationBy(user);

        huaUserRepository.save(user);
    }

    private HuaUser findUserBy(Long id) {
        return huaUserRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException("Ο χρήστης δεν βρέθηκε."));
    }

    private InformationDTO toUserDTO(HuaUser user) {
        InformationDTO dto = new InformationDTO();

        PersonalInformationDTO personalInformationDTO = setBasicInformation(user);

        dto.setPersonalInformation(personalInformationDTO);

        return dto;
    }

    private PersonalInformationDTO setBasicInformation(HuaUser user) {
        PersonalInformationDTO dto = new PersonalInformationDTO();

        dto.setId(user.getId());

        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setCreatedDate(user.getDateCreated());
        dto.setUsername(user.getUsername());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setVatNumber(user.getVatNumber());

        String birthDateFormatted = formatDateToString(user.getBirthDate());
        dto.setBirthDate(birthDateFormatted);

        String hireDateFormatted = formatDateToString(user.getHireDate());
        dto.setHireDate(hireDateFormatted);

        dto.setBusinessEmail(user.getBusinessEmail());
        dto.setPersonalEmail(user.getPersonalEmail());

        dto.setGender(user.getGender() != null ? user.getGender().name() : null);
        dto.setEmployeeStatus(user.getEmployeeStatus() != null ? user.getEmployeeStatus().name() : null);
        dto.setMaritalStatus(user.getMaritalStatus() != null ? user.getMaritalStatus().name() : null);

        return dto;
    }

    private void updateBasicInformationBy(HuaUser user) {
        PersonalInformationDTO dto = new PersonalInformationDTO();

        //basic
        user.setSurname(dto.getSurname());
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setVatNumber(dto.getVatNumber());
        user.setGender(GenderEnum.valueOf(dto.getGender()));
        user.setMaritalStatus(MaritalStatusEnum.valueOf(dto.getMaritalStatus()));
        user.setEmployeeStatus(EmployeeStatusEnum.valueOf(dto.getEmployeeStatus()));

        String birthDate = dto.getBirthDate();

        Date birthDateFormatted = formatStringToDate(birthDate);
        user.setBirthDate(birthDateFormatted);

        //address
        user.setStreet1(dto.getStreet1());
        user.setStreet2(dto.getStreet2());
        user.setCity(dto.getCity());
        user.setProvince(dto.getProvince());
        user.setPostalCode(dto.getPostalCode());
        user.setCountry(dto.getCountry());

        //contact
        user.setMobileNumber(dto.getMobileNumber());
        user.setWorkNumber(dto.getWorkNumber());
        user.setHomeNumber(dto.getHomeNumber());
        user.setBusinessEmail(dto.getBusinessEmail());
        user.setPersonalEmail(dto.getPersonalEmail());

        //social
        user.setLinkedinUrl(dto.getLinkedinUrl());
        user.setTwitterUrl(dto.getTwitterUrl());
        user.setFacebookUrl(dto.getFacebookUrl());

        //education


        user.setLastModificationDate(LocalDateTime.now());
    }
}
