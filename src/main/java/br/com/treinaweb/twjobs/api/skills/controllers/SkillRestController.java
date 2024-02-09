package br.com.treinaweb.twjobs.api.skills.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.treinaweb.twjobs.api.skills.dtos.SkillRequest;
import br.com.treinaweb.twjobs.api.skills.dtos.SkillResponse;
import br.com.treinaweb.twjobs.api.skills.mappers.SkillMapper;
import br.com.treinaweb.twjobs.core.exceptions.SkillNotFoundException;
import br.com.treinaweb.twjobs.core.repositories.SkillRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skills")
public class SkillRestController {

    private final SkillMapper skillMapper;
    private final SkillRepository skillRepository;

    @GetMapping
    public List<SkillResponse> findAll() {
        return skillRepository.findAll()
            .stream()
            .map(skillMapper::toSkillResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public SkillResponse findById(@PathVariable Long id) {
        return skillRepository.findById(id)
            .map(skillMapper::toSkillResponse)
            .orElseThrow(SkillNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public SkillResponse create(@Valid @RequestBody SkillRequest skillRequest) {
        var skill = skillMapper.toSkill(skillRequest);
        skill = skillRepository.save(skill);
        return skillMapper.toSkillResponse(skill);
    }
    
}
