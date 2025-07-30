package Controllers;

import Services.HostCommentService;
import Services.IService;
import model.AccommodationComment;
import model.HostComment;
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
@RequestMapping("/api/host-comments")
@ComponentScan(basePackageClasses = IService.class)
public class HostCommentController {

    @Autowired
    private HostCommentService hostCommentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<HostComment>> getComments() {
        Collection<HostComment> comments = hostCommentService.findAll();
        return new ResponseEntity<Collection<HostComment>>(comments, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HostComment> getComment(@PathVariable("id") Long id) {
        HostComment comment = hostCommentService.findOne(id);

        if (comment == null) {
            return new ResponseEntity<HostComment>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<HostComment>(comment, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @GetMapping(value = "/can-rate/{hostId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> canRate(@PathVariable("hostId") Long hostId,
                                           @PathVariable("userId") Long userId) {
        return new ResponseEntity<Boolean>(hostCommentService.canRate(hostId, userId), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<HostComment> createComment(@RequestBody HostComment comment) throws Exception {
        hostCommentService.create(comment);
        return new ResponseEntity<HostComment>(comment, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('GUEST') or hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HostComment> deleteComment(@PathVariable("id") Long id) {
        hostCommentService.delete(id);
        return new ResponseEntity<HostComment>(HttpStatus.NO_CONTENT);
    }
}
