package service;

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
    public PersonalInformationDTO findPersonalInfo(Long id) {
        HuaUser user = findUserBy(id);

        return toPersonalInformationDTO(user);
    }

    @Override
    public void updatePersonalInfo(Long id, PersonalInformationDTO dto) {
        HuaUser user = findUserBy(id);

        updatePersonalInfoBy(user, dto);

        huaUserRepository.save(user);
    }


    private HuaUser findUserBy(Long id) {
        return huaUserRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException("Ο χρήστης δεν βρέθηκε."));
    }

    private PersonalInformationDTO toPersonalInformationDTO(HuaUser user) {
        PersonalInformationDTO dto = new PersonalInformationDTO();

        dto.setId(user.getId());

        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setCreatedDate(user.getDateCreated());
        dto.setUsername(user.getUsername());
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

        dto.setStreet1(user.getStreet1());
        dto.setStreet2(user.getStreet2());
        dto.setCity(user.getCity());
        dto.setProvince(user.getProvince());
        dto.setPostalCode(user.getPostalCode());
        dto.setCountry(user.getCountry());

        dto.setWorkNumber(user.getWorkNumber());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setHomeNumber(user.getHomeNumber());

        dto.setBusinessEmail(user.getBusinessEmail());
        dto.setPersonalEmail(user.getPersonalEmail());

        dto.setLinkedinUrl(user.getLinkedinUrl());
        dto.setTwitterUrl(user.getTwitterUrl());
        dto.setFacebookUrl(user.getFacebookUrl());

        return dto;
    }

    private void updatePersonalInfoBy(HuaUser user, PersonalInformationDTO dto) {
        //basic
        user.setSurname(dto.getSurname());
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setVatNumber(dto.getVatNumber());
        user.setGender(dto.getGender() != null ?
                GenderEnum.valueOf(dto.getGender()) : null);
        user.setMaritalStatus(dto.getMaritalStatus() != null ?
                MaritalStatusEnum.valueOf(dto.getMaritalStatus()) : null);
        user.setEmployeeStatus(dto.getEmployeeStatus() != null ?
                EmployeeStatusEnum.valueOf(dto.getEmployeeStatus()) : null);

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
