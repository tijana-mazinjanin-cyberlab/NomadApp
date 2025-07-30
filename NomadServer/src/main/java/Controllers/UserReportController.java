package Controllers;

import DTO.AccommodationDTO;
import DTO.UserReportDetailsDTO;
import DTO.UserReportDto;
import Services.IService;
import Services.UserReportService;
import Services.UserService;
import model.Accommodation;
import model.AppUser;
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

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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
@RequestMapping("/api/user_reports")
@ComponentScan(basePackageClasses = IService.class)
public class UserReportController {

    @Autowired
    private UserReportService userReportService;

    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserReport>> getUserReports() {
        Collection<UserReport> userReports = userReportService.findAll();
        return new ResponseEntity<Collection<UserReport>>(userReports, HttpStatus.OK);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "details")
    public ResponseEntity<Collection<UserReportDetailsDTO>> getUserReportDetails() {
        Collection<UserReport> userReports = userReportService.findAll();
        Collection<UserReportDetailsDTO> userReportDetails = userReports.stream().map(this::toDetails).toList();
        return new ResponseEntity<Collection<UserReportDetailsDTO>>(userReportDetails, HttpStatus.OK);
    }



    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReport> getUserReport(@PathVariable("id") Long id) {
        UserReport userReport = userReportService.findOne(id);

        if (userReport == null) {
            return new ResponseEntity<UserReport>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST') or hasAuthority('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReportDto> createUserReport(@RequestBody UserReportDto userReportDto) throws Exception {
        UserReport userReport = this.convertToEntity(userReportDto);
        userReportService.create(userReport);
        return new ResponseEntity<UserReportDto>(userReportDto, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserReport> deleteUserReport(@PathVariable("id") Long id) {
        userReportService.delete(id);
        return new ResponseEntity<UserReport>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "archive/{id}")
    public ResponseEntity<UserReport> archiveUserReport(@PathVariable("id") Long id) {
        UserReport report = userReportService.findOne(id);
        report.setReportStatus(ReportStatus.ARCHIVED);
        userReportService.update(report);
        return new ResponseEntity<UserReport>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "accept/{id}")
    public ResponseEntity<UserReport> acceptUserReport(@PathVariable("id") Long id) {
        UserReport report = userReportService.findOne(id);
        report.setReportStatus(ReportStatus.ACCEPTED);
        report.getReportedUser().setSuspended(true);
        userService.update(report.getReportedUser());
        userReportService.update(report);
        return new ResponseEntity<UserReport>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReport> updateUserReport(@RequestBody UserReport userReport, @PathVariable Long id)
            throws Exception {
        UserReport userReportForUpdate = userReportService.findOne(id);;
        userReportForUpdate.copyValues(userReport);

        userReportService.update(userReportForUpdate);

        if (userReportForUpdate == null) {
            return new ResponseEntity<UserReport>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
    }

    public UserReport convertToEntity(UserReportDto dto) {
        UserReport ur = new UserReport();
        ur.setReportedUser(userService.findOne(dto.getReportedUser()));
        ur.setReportingUser(userService.findOne(dto.getReportingUser()));
        ur.setReason(dto.getReason());
        ur.setReportStatus(dto.getReportStatus());
        return ur;
    }

    public UserReportDto convertToDto(UserReport ur) {
        UserReportDto dto = new UserReportDto();
        dto.setReportingUser(ur.getReportingUser().getId());
        dto.setReportedUser(ur.getReportedUser().getId());
        dto.setReason(ur.getReason());
        dto.setReportStatus(ur.getReportStatus());
        return dto;
    }
    private UserReportDetailsDTO toDetails(UserReport ur) {
        UserReportDetailsDTO dto = new UserReportDetailsDTO();
        dto.setReportingUser(ur.getReportingUser().getId());
        dto.setReportedUser(ur.getReportedUser().getId());
        dto.setReason(ur.getReason());
        dto.setReportStatus(ur.getReportStatus());
        dto.setReportedUserName(ur.getReportedUser().getUsername());
        dto.setReportingUserName(ur.getReportingUser().getUsername());
        dto.setId(ur.getId());
        return dto;
    }
}
