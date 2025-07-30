package Controllers;

import DTO.*;
import Services.*;
import model.Comment;
import model.CommentReport;
import model.UserReport;
import model.enums.ReportStatus;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/api/comment-reports")
@ComponentScan(basePackageClasses = IService.class)
public class CommentReportController {
    @Autowired
    private CommentReportService commentService;
    @Autowired
    private HostRatingService hostRatingService;
    @Autowired
    private AccommodationRatingService comService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CommentReportIdDTO>> getReports() {
        Collection<CommentReport> comments = commentService.findAll();
        Collection<CommentReportIdDTO> commentsDTOS = comments.stream().map(this::convertToIdDTO).toList();
        return new ResponseEntity<Collection<CommentReportIdDTO>>(commentsDTOS, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CommentReportDetailsDTO>> getReportDetails() {
        Collection<CommentReport> comments = commentService.findAll();
        return new ResponseEntity<Collection<CommentReportDetailsDTO>>(comments.stream().map(this::convertToDetailsDto).toList(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentReportDTO> getReport(@PathVariable("id") Long id) {
        CommentReport comment = commentService.findOne(id);

        if (comment == null) {
            return new ResponseEntity<CommentReportDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<CommentReportDTO>(this.convertToDto(comment), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('HOST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddCommentReportDTO> createReport(@RequestBody AddCommentReportDTO commentDTO) throws Exception {
        CommentReport comment = this.convertAddToEntity(commentDTO);
        commentService.create(comment);
        return new ResponseEntity<AddCommentReportDTO>(commentDTO, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CommentReportDTO> deleteReport(@PathVariable("id") Long id) {
        commentService.delete(id);
        return new ResponseEntity<CommentReportDTO>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAuthority('ADMIN')")

    @PutMapping(value = "accept/{id}")
    public ResponseEntity<CommentReportDTO> acceptReport(@PathVariable("id") Long id) {
        CommentReport report = commentService.findOne(id);
        report.setReportStatus(ReportStatus.ACCEPTED);
        comService.delete(report.getReportedRating().getId());
        commentService.update(report);
        return new ResponseEntity<CommentReportDTO>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAuthority('ADMIN')")

    @PutMapping(value = "archive/{id}")
    public ResponseEntity<CommentReportDTO> archiveReport(@PathVariable("id") Long id) {
        CommentReport report = commentService.findOne(id);
        report.setReportStatus(ReportStatus.ARCHIVED);
        commentService.update(report);
        return new ResponseEntity<CommentReportDTO>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentReportDTO> updateReport(@RequestBody CommentReportDTO commentDTO, @PathVariable Long id)
            throws Exception {
        CommentReport commentForUpdate = commentService.findOne(id);
        CommentReport updatedComment = this.convertToEntity(commentDTO);
        commentForUpdate.copyValues(updatedComment);

        commentService.update(commentForUpdate);

        if (updatedComment == null) {
            return new ResponseEntity<CommentReportDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CommentReportDTO>(commentDTO, HttpStatus.OK);
    }


    private CommentReportDTO convertToDto(CommentReport comment) {
        CommentReportDTO commentDTO = modelMapper.map(comment, CommentReportDTO.class);

        return commentDTO;
    }
    private CommentReport convertToEntity(CommentReportDTO commentDTO) {
        return modelMapper.map(commentDTO, CommentReport.class);
    }
    private CommentReport convertAddToEntity(AddCommentReportDTO commentDTO) {
        CommentReport commentReport = new CommentReport();
        commentReport.setReportedComment(comService.findOne(commentDTO.getReportedComment()));
        if(comService.findOne(commentDTO.getReportedComment()) == null){
            commentReport.setReportedComment(hostRatingService.findOne(commentDTO.getReportedComment()));

        }
        commentReport.setReportingUser(userService.findOne(commentDTO.getReportingAppUser()));
        commentReport.setReportStatus(ReportStatus.PENDING);
        commentReport.setReason(commentDTO.getReason());
        return commentReport;
    }
    private AddCommentReportDTO convertAddToDTO(CommentReport report){
        AddCommentReportDTO dto = new AddCommentReportDTO();
        dto.setReason(report.getReason());
        dto.setReportingAppUser(report.getReportingUser().getId());
        dto.setReportedComment(report.getReportedRating().getId());
        dto.setReportStatus(report.getReportStatus());
        return dto;
    }
    private CommentReportIdDTO convertToIdDTO(CommentReport report){
        CommentReportIdDTO dto = new CommentReportIdDTO();
        dto.setReason(report.getReason());
        dto.setReportingAppUser(report.getReportingUser().getId());
        dto.setReportedComment(report.getReportedRating().getId());
        dto.setReportStatus(report.getReportStatus());
        dto.setId(report.getId());
        return dto;
    }
    private CommentReportDetailsDTO convertToDetailsDto(CommentReport report){
        CommentReportDetailsDTO dto = new CommentReportDetailsDTO ();
        dto.setReason(report.getReason());
        dto.setReportingUserName(report.getReportingUser().getUsername());
        dto.setReportedUserName(report.getReportedRating().getUser().getUsername());
        dto.setReportedCommentRating(report.getReportedRating().getRating());
        dto.setReportedCommentText(report.getReportedRating().getText());
        dto.setReportStatus(report.getReportStatus());
        dto.setId(report.getId());
        return dto;
    }
}
