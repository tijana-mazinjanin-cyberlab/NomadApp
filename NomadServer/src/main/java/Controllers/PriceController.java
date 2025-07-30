package Controllers;

import DTO.NotificationDTO;
import Services.IService;
import model.Notification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/api/prices")
@ComponentScan(basePackageClasses = IService.class)

public class PriceController {
    //config
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Double>> getPrices() {
        return new ResponseEntity<Collection<Double>>(HttpStatus.OK);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getPrice(@PathVariable("id") Long id, @RequestParam Date date) {

        return new ResponseEntity<Double>(0.0, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HOST')")
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> addPrice(@PathVariable("id") Long id, @RequestParam Date date, @RequestParam Double price) {

        return new ResponseEntity<Double>(0.0, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('HOST')")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> updatePrice(@PathVariable("id") Long id, @RequestParam Date date, @RequestParam Double price) {

        return new ResponseEntity<Double>(0.0, HttpStatus.OK);
    }
}
