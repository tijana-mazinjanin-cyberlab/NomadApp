package Controllers;


import DTO.AccommodationDTO;
import DTO.ReservationDTO;
import Services.AccommodationService;
import Services.IService;
import Services.ReservationService;
import Services.UserService;
import exceptions.NotValidException;
import model.Accommodation;
import model.DateRange;
import model.Guest;
import model.Reservation;
import model.enums.ConfirmationType;
import model.enums.ReservationStatus;
import org.mockito.internal.matchers.Null;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import util.Helper;

import javax.print.attribute.standard.Media;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
@RequestMapping("/api/reservations")
@ComponentScan (basePackageClasses = IService.class)
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> getReservations() {
        Collection<Reservation> reservations = reservationService.findAll();
        Collection<ReservationDTO> reservationDTOS = reservations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<ReservationDTO>>(reservationDTOS, HttpStatus.OK);
    }

    @GetMapping (value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.findOne(id);
        if(reservation == null) { return new ResponseEntity<ReservationDTO>(HttpStatus.NOT_FOUND); }

        return new ResponseEntity<ReservationDTO>(this.convertToDto(reservation), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('HOST')")
    @GetMapping (value = "/with-host/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> getReservationForUser(@PathVariable("id") Long id) {
        System.out.println("WITH HOST");
        Collection<ReservationDTO> reservations = reservationService.findReservationsForHost(id).stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<ReservationDTO>>(reservations, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @GetMapping (value = "/with-guest/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> getReservationForGuest(@PathVariable("id") Long id) {
        System.out.println("WITH GUEST");
        Collection<ReservationDTO> reservations = reservationService.findReservationsForGuest(id).stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<ReservationDTO>>(reservations, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('HOST')")
    @PutMapping (value = "/confirm/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> verifyReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.findOne(id);
        boolean succesfull = reservationService.verify(reservation);

        if(!succesfull){
            return new ResponseEntity<Long>(id, HttpStatus.NOT_FOUND);
        }
        //reservationService.declineOverlaping(reservation);
        ReservationDTO reservationDTO = convertToDto(reservation);
        return new ResponseEntity<Long>( id, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('HOST')")
    @PutMapping (value = "/reject/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> declineReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.findOne(id);
        if (reservation == null){
            return new ResponseEntity<Long>(id, HttpStatus.NOT_FOUND);
        }
        reservationService.decline(reservation);
        ReservationDTO reservationDTO = convertToDto(reservation);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @PutMapping (value = "/cancel/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> cancelReservation(@PathVariable("id") Long id) {
        System.out.println("Cancel");
        try{
            reservationService.cancel(id);
        }catch(NotValidException r){
            return new ResponseEntity<Long>(id, HttpStatus.BAD_REQUEST);
        }catch (NullPointerException n){
            return new ResponseEntity<Long>(id,HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('GUEST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> createReservation (@RequestBody ReservationDTO reservationDTO) {

        Reservation newReservation = this.convertToEntity(reservationDTO);
        if(!reservationService.validateReservation(newReservation)){
            return new ResponseEntity<ReservationDTO>(reservationDTO, HttpStatus.BAD_REQUEST);
        }
        if(newReservation.getAccommodation().getConfirmationType() == ConfirmationType.AUTOMATIC){
            if(reservationService.reserveAutomatically(newReservation)){
                return new ResponseEntity<ReservationDTO>(reservationDTO, HttpStatus.CREATED);
            }
        }else{
            if(reservationService.reserveManually(newReservation)){
                return new ResponseEntity<ReservationDTO>(reservationDTO, HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<ReservationDTO>(HttpStatus.FORBIDDEN);
    }

    @PreAuthorize("hasAuthority('GUEST')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long>deleteReservation(@PathVariable("id") Long id) {
        System.out.println("DELETE");
        reservationService.delete(id);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    @PutMapping (value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> updateReservation(@RequestBody ReservationDTO reservationDTO, @PathVariable Long id) {
        Reservation reservationForUpdate = reservationService.findOne(id);
        Reservation updatedReservation = this.convertToEntity(reservationDTO);
        reservationForUpdate.copyValues(updatedReservation);
        reservationService.update(reservationForUpdate);

        if (updatedReservation == null) {return new ResponseEntity<ReservationDTO>(HttpStatus.INTERNAL_SERVER_ERROR);}

        return new ResponseEntity<ReservationDTO>(reservationDTO, HttpStatus.OK);
    }
   @PreAuthorize("hasAuthority('HOST')")
    @GetMapping(value = "/search-host/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> searchHost(@PathVariable("id") Long id, @RequestParam(required = true) String name,
                                                                         @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date minimumDate, @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date maximumDate,
                                                                         @RequestParam(required = false) ReservationStatus status) {
        Collection<Reservation> reservations = reservationService.getFilteredHost(id, name, minimumDate, maximumDate, status);
        Collection<ReservationDTO> reservationDTOS = reservations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<ReservationDTO>>(reservationDTOS, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('HOST')")
    @GetMapping(value = "/filter-host/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> filterHost(@PathVariable("id") Long id, @RequestParam(required = true) ReservationStatus status) {
        Collection<Reservation> reservations = reservationService.getFilteredHost(id, "", null, null, status);
        Collection<ReservationDTO> reservationDTOS = reservations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<ReservationDTO>>(reservationDTOS, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @GetMapping(value = "/search-guest/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> searchGuest(@PathVariable("id") Long id, @RequestParam(required = true) String name,
                                                                         @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date minimumDate, @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date maximumDate,
                                                                         @RequestParam(required = false) ReservationStatus status) {
        Collection<Reservation> reservations = reservationService.getFilteredGuest(id, name, minimumDate, maximumDate, status);
        Collection<ReservationDTO> reservationDTOS = reservations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<ReservationDTO>>(reservationDTOS, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @GetMapping(value = "/filter-guest/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> filterGuest(@PathVariable("id") Long id, @RequestParam(required = true) ReservationStatus status) {
        Collection<Reservation> reservations = reservationService.getFilteredGuest(id, "", null, null, status);
        Collection<ReservationDTO> reservationDTOS = reservations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<ReservationDTO>>(reservationDTOS, HttpStatus.OK);
    }

    private ReservationDTO convertToDto(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUser(reservation.getUser().getId());
        reservationDTO.setAccommodation(reservation.getAccommodation().getId());
        reservationDTO.setNumGuests(reservation.getNumGuests());
        reservationDTO.setStartDate(reservation.getDateRange().getStartDate());
        reservationDTO.setFinishDate(reservation.getDateRange().getFinishDate());
        reservationDTO.setStatus(reservation.getStatus());
        reservationDTO.setId(reservation.getId());
        return reservationDTO;
    }
    private Reservation convertToEntity(ReservationDTO reservationDTO) {
        Reservation reservation =  modelMapper.map(reservationDTO, Reservation.class);
        reservation.setDateRange(new DateRange(Helper.setMiliseconds(reservationDTO.getStartDate()), Helper.setMiliseconds(reservationDTO.getFinishDate())));
        reservation.setUser((Guest)userService.findOne(reservationDTO.getUser()));
        reservation.setAccommodation(accommodationService.findOne(reservationDTO.getAccommodation()));
        reservation.setId(reservationDTO.getId());
        return reservation;
    }

}
