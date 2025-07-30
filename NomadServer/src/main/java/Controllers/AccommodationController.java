package Controllers;

import DTO.AccommodationDTO;
import DTO.AccommodationDTOReport;
import DTO.SearchAccommodationDTO;
import DTO.UserDTO;
import Services.AccommodationService;
import Services.AmenityService;
import Services.IService;
import Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import model.*;
import model.enums.AccommodationStatus;
import model.enums.AccommodationType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.CollectionEndEvent;

import javax.sound.midi.SysexMessage;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.Double.MAX_VALUE;

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
@RequestMapping("/api/accommodations")
@ComponentScan(basePackageClasses = IService.class)
public class AccommodationController {
    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private AmenityService amenityService;
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;
    //config
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<AccommodationDTO>> getAccommodations() {
        Collection<Accommodation> accommodations = accommodationService.findAll();
        Collection<AccommodationDTO> accommodationDTOS = accommodations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<AccommodationDTO>>(accommodationDTOS, HttpStatus.OK);
    }
    //@PreAuthorize("hasAuthority('HOST') or hasAuthority('ADMIN') or hasAuthority('GUEST')")
    @GetMapping(value = "/verified", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<AccommodationDTO>> getApprovedAccommodations() {
        Collection<Accommodation> accommodations = accommodationService.findApprovedAccommodations();
        Collection<AccommodationDTO> accommodationDTOS = accommodations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<AccommodationDTO>>(accommodationDTOS, HttpStatus.OK);
    }
  
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/unverified", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<AccommodationDTO>> getUnverifiedAccommodations() {
        Collection<Accommodation> accommodations = accommodationService.getUnverifiedAccommodations();
        Collection<AccommodationDTO> accommodationDTOS = accommodations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<AccommodationDTO>>(accommodationDTOS, HttpStatus.OK);
    }
    //config
    @GetMapping(value = "/search-filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SearchAccommodationDTO>> searchAndFIlterccommodations(@RequestParam(required = true) String city,
                             @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date from, @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date to,
                             @RequestParam(required = true) int peopleNum, @RequestParam(required = false) Double minimumPrice,
                             @RequestParam(required = false) Double maximumPrice, @RequestParam(required = false) List<Long> amenity,
                             @RequestParam(required = false) AccommodationType type) {


        if(maximumPrice == null && minimumPrice != null){
            maximumPrice = MAX_VALUE;
        }else if(maximumPrice != null && minimumPrice == null){
            minimumPrice = 0.0;
        }

        Collection<SearchAccommodationDTO> accommodationsDTOs = accommodationService.getSearchedAndFiltered(city, new DateRange(from, to), peopleNum, minimumPrice,
                                                    maximumPrice, amenity, type);

        return new ResponseEntity<Collection<SearchAccommodationDTO>>(accommodationsDTOs, HttpStatus.OK);
    }
    //config
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<AccommodationDTO>> filterccommodations(@RequestParam(required = false) Double minimumPrice,
                                                                                           @RequestParam(required = false) Double maximumPrice, @RequestParam(required = false) List<Long> amenity,
                                                                                           @RequestParam(required = false) AccommodationType type) {
        if(maximumPrice == null && minimumPrice != null){
            maximumPrice = MAX_VALUE;
        }else if(maximumPrice != null && minimumPrice == null){
            minimumPrice = 0.0;
        }
        if(minimumPrice == null && maximumPrice == null && type == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Collection<Accommodation> accommodations = accommodationService.getFiltered( minimumPrice, maximumPrice, amenity, type);
        Collection<AccommodationDTO> accommodationsDTOs = accommodations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<AccommodationDTO>>(accommodationsDTOs, HttpStatus.OK);
    }
    //config
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> getAccommodation(@PathVariable("id") Long id) {
        Accommodation accommodation = accommodationService.findOne(id);

        if (accommodation == null) {
            return new ResponseEntity<AccommodationDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<AccommodationDTO>(this.convertToDto(accommodation), HttpStatus.OK);
    }
    //config
    @GetMapping("/{accommodationId}/amenities")
    public ResponseEntity<Collection<Amenity>> getAmenitiesForAccommodation(@PathVariable long accommodationId) {
        return new ResponseEntity<Collection<Amenity>>(accommodationService.getAllAmenitiesForAccommodation(accommodationId), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('HOST')")
    @GetMapping("/host/{hostId}")
    public ResponseEntity<Collection<AccommodationDTO>> getAccommodationsForHost(@PathVariable("hostId") Long hostId) {
        Collection<Accommodation> accommodations = this.accommodationService.findByHost(hostId);
        System.out.println(accommodations.size());
        Collection<AccommodationDTO> accommodationDTOS = accommodations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<AccommodationDTO>>(accommodationDTOS, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('HOST')")
    @GetMapping("/host-reports/{hostId}")
    public ResponseEntity<Collection<AccommodationDTOReport>> getAccommodationsForHostReports(@PathVariable("hostId") Long hostId) {
        Collection<Accommodation> accommodations = this.accommodationService.findByHost(hostId);
        System.out.println(accommodations.size());
        Collection<AccommodationDTOReport> accommodationDTOS = accommodations.stream().map(this::convertToDto2).toList();
        return new ResponseEntity<Collection<AccommodationDTOReport>>(accommodationDTOS, HttpStatus.OK);
    }
    //config
    @GetMapping("isAvailable/{accommodationId}/{date}")
    public ResponseEntity<Boolean> isAvailable(@PathVariable long accommodationId, @PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return new ResponseEntity<Boolean>(accommodationService.isAvailable(accommodationId, date), HttpStatus.OK);
    }
    //config
    @GetMapping("taken-dates/{accommodationId}")
    public ResponseEntity<List<Date>> getAccommodationTakenDates(@PathVariable long accommodationId) {
        System.out.println("TAKEN DATESS");
        return new ResponseEntity<List<Date>>(accommodationService.getTakenDates(accommodationId), HttpStatus.OK);
    }
    //config
    @GetMapping("price/{accommodationId}/{date}")
    public ResponseEntity<Double> getPrice(@PathVariable long accommodationId, @PathVariable  Date date) {
        try{
            return new ResponseEntity<Double>(accommodationService.getPrice(accommodationId, date), HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<Double>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("price/{accommodationId}")
    public ResponseEntity<String> setPrice(@PathVariable long accommodationId, @RequestBody Map<String, Object> requestBody){
        double price = Double.parseDouble((String) requestBody.get("price"));
        String startDateStr = (String) requestBody.get("startDate");
        String finishDateStr = (String) requestBody.get("finishDate");
        DateRange dateRange;
        try{
            dateRange = new DateRange(startDateStr, finishDateStr);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<String>("Illegal date range", HttpStatus.BAD_REQUEST);
        }
        if(!this.accommodationService.setPriceForDateRange(accommodationId, price, dateRange)){
            return new ResponseEntity<String>("Date range in past", HttpStatus.BAD_REQUEST);
        }
        String message = "Price is successfully updated for date range: " + dateRange.toString();
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
    @PostMapping("unavailable/{accommodationId}")
    public ResponseEntity<String> setUnavailable(@PathVariable long accommodationId, @RequestBody Map<String, Object> requestBody){
        System.out.println(requestBody.toString());
        String startDateStr = (String) requestBody.get("startDate");
        String finishDateStr = (String) requestBody.get("finishDate");
        DateRange dateRange;
        try{
            dateRange = new DateRange(startDateStr, finishDateStr);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<String>("invalid date range", HttpStatus.BAD_REQUEST);
        }
        String message = "Accommodation doesn't exist";
        try{
            this.accommodationService.setUnavailableForDateRange(accommodationId, dateRange);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
        }
        message = "Accommodation is set to unavailable for dates: " + dateRange.toString();
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @PostMapping("available/{accommodationId}")
    public ResponseEntity<String> setAvailable(@PathVariable long accommodationId, @RequestBody Map<String, Object> requestBody){
        String startDateStr = (String) requestBody.get("startDate");
        String finishDateStr = (String) requestBody.get("finishDate");
        DateRange dateRange = new DateRange(startDateStr, finishDateStr);
        this.accommodationService.setAvailableForDateRange(accommodationId, dateRange);
        String message = "Accommodation is set to available for dates: " + dateRange.toString();
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HOST')")
    @PostMapping("/{accommodationId}/amenities")
    public ResponseEntity<Amenity> addAmenityToAccommodation(@PathVariable long accommodationId, @RequestBody Amenity newAmenity) {
        accommodationService.addAmenityToAccommodation(accommodationId, newAmenity);
        return new ResponseEntity<Amenity>(newAmenity, HttpStatus.CREATED);
    }

    //@PreAuthorize("hasAuthority('HOST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> createAccommodation(@RequestBody AccommodationDTO accommodationDTO) throws Exception {
        Accommodation accommodation = this.convertToEntity(accommodationDTO);
        if(!accommodationService.validateAccommodation(accommodation)){
            return new ResponseEntity<AccommodationDTO>(accommodationDTO, HttpStatus.BAD_REQUEST);
        }
        accommodation.setHost( (Host) userService.findOne(accommodationDTO.getHostId()) );
        accommodationDTO = convertToDto(accommodationService.createAccommodation(accommodation));
        return new ResponseEntity<AccommodationDTO>(accommodationDTO, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AccommodationDTO> deleteAccommodation(@PathVariable("id") Long id) {
        accommodationService.delete(id);
        return new ResponseEntity<AccommodationDTO>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<AccommodationDTO> updateAccommodation(@RequestBody AccommodationDTO accommodationDTO, @PathVariable Long id)
            throws Exception {
        Accommodation accommodationForUpdate = accommodationService.findOne(id);
        if(accommodationForUpdate == null){
            return new ResponseEntity<AccommodationDTO>(HttpStatus.BAD_REQUEST);
        }
        Accommodation updatedAccommodation = this.convertToEntity(accommodationDTO);
        accommodationForUpdate.copyValues(updatedAccommodation);
        if(!accommodationService.validateAccommodation(accommodationForUpdate)){
            return new ResponseEntity<AccommodationDTO>(HttpStatus.BAD_REQUEST);
        }
        accommodationService.update(accommodationForUpdate);

        if (updatedAccommodation == null) {
            return new ResponseEntity<AccommodationDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<AccommodationDTO>(accommodationDTO, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "verify/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> updateAccommodation(@PathVariable Long id)
            throws Exception {
        Accommodation accommodationForUpdate = accommodationService.findOne(id);
        accommodationForUpdate.setVerified(true);
        accommodationForUpdate.setStatus(AccommodationStatus.APPROVED);
        accommodationService.update(accommodationForUpdate);
        return new ResponseEntity<AccommodationDTO>(convertToDto(accommodationForUpdate), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @PutMapping(value = "/favourite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> favouriteAccommodation(@RequestParam() Long userID, @RequestParam() Long accommodationID) {
        Accommodation accommodation = this.accommodationService.findOne(accommodationID);
        if(accommodation == null){
            return new ResponseEntity<AccommodationDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AccommodationDTO>(this.convertToDto(accommodation),HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @PutMapping(value = "/un-favourite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> unFavouriteAccommodation(@RequestParam() Long userID, @RequestParam() Long accommodationID) {
        Accommodation accommodation = this.accommodationService.findOne(accommodationID);
        if(accommodation == null){
            return new ResponseEntity<AccommodationDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AccommodationDTO>(this.convertToDto(accommodation),HttpStatus.OK);
    }
    private AccommodationDTO convertToDto(Accommodation accommodation) {
        AccommodationDTO accommodationDTO = modelMapper.map(accommodation, AccommodationDTO.class);
        accommodationDTO.setId(accommodation.getId());
        return accommodationDTO;
    }
    private AccommodationDTOReport convertToDto2(Accommodation accommodation) {
        AccommodationDTOReport accommodationDTO = modelMapper.map(accommodation, AccommodationDTOReport.class);
        accommodationDTO.setId(accommodation.getId());
        return accommodationDTO;
    }
    private Accommodation convertToEntity(AccommodationDTO accommodationDTO) {
        Accommodation accommodation = modelMapper.map(accommodationDTO, Accommodation.class);
        accommodation.setHost((Host)userService.findOne(accommodationDTO.getHostId()));
        return accommodation;
    }

}
