package br.com.treinaweb.twjobs.api.jobs.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.treinaweb.twjobs.api.jobs.dtos.JobRequest;
import br.com.treinaweb.twjobs.api.jobs.dtos.JobResponse;
import br.com.treinaweb.twjobs.api.jobs.mappers.JobMapper;
import br.com.treinaweb.twjobs.core.exceptions.JobNotFoundException;
import br.com.treinaweb.twjobs.core.repositories.JobRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobRestController {

    private final JobMapper jobMapper;
    private final JobRepository jobRepository;

    @GetMapping
    public List<JobResponse> findAll() {
        return jobRepository.findAll()
            .stream()
            .map(jobMapper::toJobResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public JobResponse findById(@PathVariable Long id) {
        var job = jobRepository.findById(id)
            .orElseThrow(JobNotFoundException::new);
        return jobMapper.toJobResponse(job);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public JobResponse create(@RequestBody @Valid JobRequest jobRequest) {
        var job = jobMapper.toJob(jobRequest);
        job = jobRepository.save(job);
        return jobMapper.toJobResponse(job);
    }
    
}
