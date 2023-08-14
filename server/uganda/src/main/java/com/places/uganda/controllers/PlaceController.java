package com.places.uganda.controllers;


import com.places.uganda.entities.Place;
import com.places.uganda.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class PlaceController {

    @Autowired
    PlaceRepository placeRepository;

    @GetMapping("/ugandaplaces")
    public ResponseEntity<List<Place>>getAllPlaces(@RequestParam(required = false)String name){
        try {
            List<Place>places = new ArrayList<>();

            if (name == null)
                placeRepository.findAll().forEach(places::add);
            else
                placeRepository.findByNameContainingIgnoreCase(name).forEach(places::add);
            if (places.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ugandaplaces/{id}")
    public ResponseEntity<Place>getPlaceById(@PathVariable("id") long id){
        Optional<Place>placeData = placeRepository.findById(id);
        if (placeData.isPresent()){
            return new ResponseEntity<>(placeData.get(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/ugandaplaces")
    public ResponseEntity<Place>createPlace(@RequestBody Place place){
        try{
            Place _place = placeRepository
                    .save(new Place(place.getName()));
            return new ResponseEntity<>(_place, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/ugandaplaces/{id}")
    public ResponseEntity<Place>updatePlace(@PathVariable("id") long id, @RequestBody Place place){
        Optional<Place> placeData = placeRepository.findById(id);

        if (placeData.isPresent()){
            Place _place = placeData.get();
            _place.setName(place.getName());

            return new ResponseEntity<>(placeRepository.save(_place), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/ugandaplaces/{id}")
    public ResponseEntity<HttpStatus>deletePlace(@PathVariable("id") long id) {
        try {
            placeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/ugandaplaces")
    public ResponseEntity<HttpStatus>deleteAllPlaces(){
        try{
            placeRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
