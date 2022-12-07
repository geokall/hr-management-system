package service;

import dto.EducationDTO;
import dto.PersonalInformationDTO;
import entity.HuaEducation;
import entity.HuaUser;
import enums.DegreeEnum;
import enums.EmployeeStatusEnum;
import enums.GenderEnum;
import enums.MaritalStatusEnum;
import exception.HuaNotFoundException;
import repository.HuaEducationRepository;
import repository.HuaUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static exception.HuaCommonError.*;
import static utils.HuaUtil.formatDateToString;
import static utils.HuaUtil.formatStringToDate;

@ApplicationScoped
public class PersonalInfoServiceImpl implements PersonalInfoService {

    private final HuaUserRepository userRepository;
    private final HuaEducationRepository educationRepository;

    @Inject
    public PersonalInfoServiceImpl(HuaUserRepository userRepository,
                                   HuaEducationRepository educationRepository) {
        this.userRepository = userRepository;
        this.educationRepository = educationRepository;
    }

    @Override
    public PersonalInformationDTO findPersonalInfo(Long id) {
        HuaUser user = findUserBy(id);

        return toPersonalInformationDTO(user);
    }

    @Override
    public List<EducationDTO> findEducations(Long id) {
        HuaUser user = findUserBy(id);

        return educationRepository.findByUser(user).stream()
                .map(this::toEducationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updatePersonalInfo(Long id, PersonalInformationDTO dto) {
        HuaUser user = findUserBy(id);

        updatePersonalInfoBy(user, dto);

        userRepository.save(user);
    }

    @Override
    public void createEducation(Long id, EducationDTO dto) {
        HuaUser user = findUserBy(id);

        HuaEducation education = new HuaEducation();
        education.setUser(user);

        saveEducationBy(dto, education);
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


    private HuaUser findUserBy(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException(USER_NOT_FOUND));
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

        List<EducationDTO> listOfEducation = user.getEducations().stream()
                .map(this::toEducationDTO)
                .collect(Collectors.toList());

        dto.setEducations(listOfEducation);

        return dto;
    }

    private EducationDTO toEducationDTO(HuaEducation education) {
        EducationDTO educationDTO = new EducationDTO();
        educationDTO.setId(education.getId());
        educationDTO.setGpa(education.getGpa());
        educationDTO.setCollege(education.getCollege());

        educationDTO.setDegree(education.getDegree() != null ?
                education.getDegree().name() : null);

        educationDTO.setSpecialization(education.getSpecialization());

        String studyFromFormatted = formatDateToString(education.getStudyFrom());
        String studyToFormatted = formatDateToString(education.getStudyTo());

        educationDTO.setStudyFrom(studyFromFormatted);
        educationDTO.setStudyTo(studyToFormatted);

        return educationDTO;
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

        user.setLastModificationDate(LocalDateTime.now());
    }

    private void saveEducationBy(EducationDTO dto, HuaEducation education) {
        education.setCollege(dto.getCollege());
        education.setGpa(dto.getGpa());

        education.setDegree(dto.getDegree() != null ?
                DegreeEnum.valueOf(dto.getDegree()) : null);

        education.setSpecialization(dto.getSpecialization());

        Date studyFrom = formatStringToDate(dto.getStudyFrom());
        Date studyTo = formatStringToDate(dto.getStudyTo());

        education.setStudyFrom(studyFrom);
        education.setStudyTo(studyTo);

        educationRepository.save(education);
    }
}
