package Controllers;

import DTO.CommentDTO;
import Services.AmenityService;
import Services.IService;
import model.Amenity;
import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(
        origins = {
                "http://localhost:8081"
        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        })

@RestController
@RequestMapping("/api/amenities")
@ComponentScan(basePackageClasses = IService.class)
public class AmenityController {
    @Autowired
    private AmenityService amenityService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Amenity>> getAmenities() {
        Collection<Amenity> amenities = amenityService.findAll();
        return new ResponseEntity<Collection<Amenity>>(amenities, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Amenity> getAmenity(@PathVariable("id") Long id) {
        Amenity amenity = amenityService.findOne(id);

        if (amenity == null) {
            return new ResponseEntity<Amenity>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Amenity>(amenity, HttpStatus.OK);
    }
    //config
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Amenity> createAmenity(@RequestBody Amenity amenity) throws Exception {
        amenityService.create(amenity);
        return new ResponseEntity<Amenity>(amenity, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Amenity> deleteAmenity(@PathVariable("id") Long id) {
        amenityService.delete(id);
        return new ResponseEntity<Amenity>(HttpStatus.NO_CONTENT);
    }
}
