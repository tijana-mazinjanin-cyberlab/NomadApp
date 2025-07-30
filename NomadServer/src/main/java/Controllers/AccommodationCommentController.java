package Controllers;

import DTO.AccommodationDTO;
import Services.IService;
import model.Accommodation;
import model.AccommodationComment;
import model.HostRating;
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
@RequestMapping("/api/accommodation-comments")
@ComponentScan(basePackageClasses = IService.class)
public class AccommodationCommentController {

        @Autowired
        private IService<AccommodationComment, Long> accommodationCommentService;

        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Collection<AccommodationComment>> getComments() {
                Collection<AccommodationComment> accommodationComments = accommodationCommentService.findAll();
                return new ResponseEntity<Collection<AccommodationComment>>(accommodationComments, HttpStatus.OK);
        }
        @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<AccommodationComment> getComment(@PathVariable("id") Long id) {
                AccommodationComment comment = accommodationCommentService.findOne(id);

                if (comment == null) {
                        return new ResponseEntity<AccommodationComment>(HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<AccommodationComment>(comment, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('GUEST')")
        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<AccommodationComment> createComment(@RequestBody AccommodationComment comment) throws Exception {
                accommodationCommentService.create(comment);
                return new ResponseEntity<AccommodationComment>(comment, HttpStatus.CREATED);
        }

        @PreAuthorize("hasAuthority('GUEST') or hasAuthority('ADMIN')")
        @DeleteMapping(value = "/{id}")
        public ResponseEntity<AccommodationComment> deleteComment(@PathVariable("id") Long id) {
                accommodationCommentService.delete(id);
                return new ResponseEntity<AccommodationComment>(HttpStatus.NO_CONTENT);
        }
}
