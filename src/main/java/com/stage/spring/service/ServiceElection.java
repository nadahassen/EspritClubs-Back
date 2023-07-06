package com.stage.spring.service;

import com.stage.spring.entity.Election;
import com.stage.spring.entity.Image;
import com.stage.spring.repository.ElectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ServiceElection implements IServiceElection {

    @Autowired
    private ElectionRepository electionRepository;
    @Autowired

    IServiceImage imageService;
    @Override
    public List<Election> retrieveAllElections() {
        return electionRepository.findAll();
    }


    @Override
    public Election retrieveElection(Long id) {

        return electionRepository.findById(id).orElse(null);
    }
    @Override
    public void addElection(Election election, MultipartFile file) throws IOException {
        List<String> filenames = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        election.setCreatedAt(date);

        Image image = new Image(file.getOriginalFilename());
        String filename1 = StringUtils.cleanPath(file.getOriginalFilename());
        filenames.add(filename1);
        election.setImage(image);
        imageService.save(file);
        electionRepository.save(election);
    }

    @Override
    public void deleteElection(Long id) {
        electionRepository.deleteById(id);

    }

    @Override
    public Election updateElection(Election election) {
        return electionRepository.save(election);
    }



    @Override
    public Boolean checkIfExist(Long id) {
        return electionRepository.existsById(id);
    }

    @Override
    public Page<Election> getElectionsPagedAndSorted(int offset, int pageSize, String field) {
        return electionRepository.findAll(PageRequest.of(offset, pageSize, Sort.by(field)));
    }

    @Override
    public Page<Election> getElectionsPaged(Pageable pageable) {
        return electionRepository.findAll(pageable);
    }
}
