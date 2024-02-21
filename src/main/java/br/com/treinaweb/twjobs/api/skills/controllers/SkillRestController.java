package br.com.treinaweb.twjobs.api.skills.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        var skills = skillRepository.findAll()
            .stream()
            .map(skillMapper::toSkillResponse)
            .toList();

        skills.forEach(skill -> {
            var id = skill.getId();

            var selfLink = linkTo(methodOn(SkillRestController.class).findById(id))
                .withSelfRel()
                .withType("GET");

            var updateLink = linkTo(methodOn(SkillRestController.class).update(id, null))
                .withRel("update")
                .withType("PUT");

            var deleteLink = linkTo(methodOn(SkillRestController.class).delete(id))
                .withRel("delete")
                .withType("DELETE");
            

            skill.add(selfLink, updateLink, deleteLink);
        });

        return skills;
    }

    @GetMapping("/{id}")
    public SkillResponse findById(@PathVariable Long id) {
        var skill = skillRepository.findById(id)
            .map(skillMapper::toSkillResponse)
            .orElseThrow(SkillNotFoundException::new);

        var selfLink = linkTo(methodOn(SkillRestController.class).findById(id))
            .withSelfRel()
            .withType("GET");

        var updateLink = linkTo(methodOn(SkillRestController.class).update(id, null))
            .withRel("update")
            .withType("PUT");

        var deleteLink = linkTo(methodOn(SkillRestController.class).delete(id))
            .withRel("delete")
            .withType("DELETE");
        

        skill.add(selfLink, updateLink, deleteLink);

        return skill;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public SkillResponse create(@Valid @RequestBody SkillRequest skillRequest) {
        var skill = skillMapper.toSkill(skillRequest);
        skill = skillRepository.save(skill);
        return skillMapper.toSkillResponse(skill);
    }

    @PutMapping("/{id}")
    public SkillResponse update(
        @PathVariable Long id, 
        @Valid @RequestBody SkillRequest skillRequest
    ) {
        var skill = skillRepository.findById(id)
            .orElseThrow(SkillNotFoundException::new);
        BeanUtils.copyProperties(skillRequest, skill, "id");
        skill = skillRepository.save(skill);
        return skillMapper.toSkillResponse(skill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var skill = skillRepository.findById(id)
            .orElseThrow(SkillNotFoundException::new);
        skillRepository.delete(skill);
        return ResponseEntity.noContent().build();
    }
    
}
