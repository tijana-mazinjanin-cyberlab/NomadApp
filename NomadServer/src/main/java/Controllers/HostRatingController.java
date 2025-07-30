package Controllers;

import DTO.RatingCreationDTO;
import DTO.RatingDTO;
import Services.HostRatingService;
import Services.IService;
import Services.UserService;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/host-ratings")
@ComponentScan(basePackageClasses = IService.class)
public class HostRatingController {

    @Autowired
    private HostRatingService hostRatingService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/host/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<RatingDTO>> getRatingsForHost(@PathVariable("id") Long id) {
        Collection<HostRating> ratings = hostRatingService.findAllRatingsForHost(id);
        return new ResponseEntity<Collection<RatingDTO>>(ratings.stream().map(this::mapRating).toList(), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @GetMapping(value = "/host-rating/{userId}/{hostId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> findCommentOnHost(@PathVariable("userId") Long userId, @PathVariable("hostId") Long hostId) {
        Long response = hostRatingService.findForUserAndHost(userId, hostId);
        if(response == -1){
            return new ResponseEntity<Long>(-1L, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Long>(response, HttpStatus.OK);

    }
    @PreAuthorize("hasAuthority('GUEST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RatingCreationDTO> createRating(@RequestBody RatingCreationDTO ratingDto) throws Exception {
        HostRating rating = this.mapRatingDTO(ratingDto);
        hostRatingService.create(rating);
        return new ResponseEntity<RatingCreationDTO>(ratingDto, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<RatingDTO> deleteRating(@PathVariable Long id) throws Exception {
        if(hostRatingService.findOne(id) == null){
            return new ResponseEntity<RatingDTO>(HttpStatus.NOT_FOUND);
        }
        hostRatingService.delete(id);
        return new ResponseEntity<RatingDTO>(HttpStatus.OK);
    }

    public RatingDTO mapRating(HostRating rating){
        RatingDTO dto = new RatingDTO();
        dto.setText(rating.getText());
        dto.setRating(rating.getRating());
        dto.setUserName(rating.getUser().getUsername());
        dto.setId(rating.getId());
        return dto;
    }
    public HostRating mapRatingDTO(RatingCreationDTO dto){
        HostRating rating = new HostRating();
        rating.setText(dto.getText());
        rating.setRating(dto.getRating());
        rating.setHost((Host) this.userService.findOne(dto.getRatedId()));
        rating.setUser(this.userService.findOne(dto.getUserId()));

        return rating;
    }
}
