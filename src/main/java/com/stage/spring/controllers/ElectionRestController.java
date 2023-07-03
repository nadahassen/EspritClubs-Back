package com.stage.spring.controllers;

import com.stage.spring.entity.Election;
import com.stage.spring.service.IServiceElection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
@Slf4j
@RestController
@RequestMapping("/Election")
@CrossOrigin(origins = "http://localhost:4200")
public class ElectionRestController {

    @Autowired
    private IServiceElection serviceElection;

    @GetMapping("/get-all-elections")
    public List<Election> getAllElections() {
        return serviceElection.retrieveAllElections();
    }

    @GetMapping("getElectionById/{id}")
    public ResponseEntity<Election> getElectionById(@PathVariable Long id) {
        Election election = serviceElection.retrieveElection(id);
        if (election != null) {
            return ResponseEntity.ok(election);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addElection")
    public ResponseEntity<Election> addElection(@RequestBody Election election) {
        Election addedElection = serviceElection.addElection(election);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedElection);
    }

    @PutMapping("/updateElection/{id}")
    public ResponseEntity<Election> updateElection(@PathVariable Long id, @RequestBody Election election) {
        if (!serviceElection.checkIfExist(id)) {
            return ResponseEntity.notFound().build();
        }
        election.setIdElection(id);
        Election updatedElection = serviceElection.updateElection(election);
        return ResponseEntity.ok(updatedElection);
    }

    @DeleteMapping("/deleteElection/{id}")
    public ResponseEntity<Void> deleteElection(@PathVariable Long id) {
        if (!serviceElection.checkIfExist(id)) {
            return ResponseEntity.notFound().build();
        }
        serviceElection.deleteElection(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paged")
    public Page<Election> getPagedElections(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField));
        return serviceElection.getElectionsPaged(pageable);
    }

/*
La fonction getPagedElections dans la classe ElectionController est un point de terminaison GET pour
récupérer une page paginée d'élections.
Elle utilise les paramètres de requête page, size et sortField pour spécifier la pagination et le tri
des résultats. Voici à quoi servent ces paramètres :

page : spécifie le numéro de la page à récupérer. La valeur par défaut est 0, ce qui signifie
que la première page sera récupérée.
size : spécifie la taille de la page, c'est-à-dire le nombre d'éléments à afficher par page.
La valeur par défaut est 10.
sortField : spécifie le champ de tri pour ordonner les résultats. La valeur par défaut est "id",
ce qui signifie que les résultats seront triés par ID d'élection.
la fonction renvoie la page d'élections récupérée en réponse à la requête GET*/

}
