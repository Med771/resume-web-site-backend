package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SetSpecialityNameReq;
import ru.ai.sin.dto.speciality.SetSpecialitySkillsReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;
import ru.ai.sin.repository.SpecialityRepo;
import ru.ai.sin.service.impl.SpecialityService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialityServImpl implements SpecialityService {

    private final SpecialityRepo specialityRepo;

    @Override
    public SpecialityDTO getById(Long id) {
        return null;
    }

    @Override
    public List<SpecialityDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public SpecialityDTO create(AddSpecialityReq addSpecialityReq) {
        return null;
    }

    @Override
    public SpecialityDTO setNameById(Long id, SetSpecialityNameReq setSpecialityNameReq) {
        return null;
    }

    @Override
    public SpecialityDTO setSkillsById(Long id, SetSpecialitySkillsReq setSpecialitySkillsReq) {
        return null;
    }

    @Override
    public SpecialityDTO deleteById(Long id) {
        return null;
    }
}
