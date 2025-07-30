package Controllers;

import DTO.AccommodationDTO;
import DTO.ReportDTO;
import Repositories.AmenityRepository;
import Services.IService;
import Services.JasperReportService;
import Services.ReservationService;
import Services.UserService;
import model.Accommodation;
import model.DateRange;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.repo.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import util.Helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
@RequestMapping("/api/reports")
@ComponentScan(basePackageClasses = IService.class)
public class ReportsController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    JasperReportService jasperReportService;
    @PreAuthorize("hasAuthority('HOST')")
    @GetMapping(value = "/date-range/{hostId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReportDTO>> getReportForDateRange(@PathVariable("hostId") Long hostId,
                                                                                   @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date from,
            @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date to) throws JRException, FileNotFoundException {


        List<ReportDTO> reports = reservationService.getReportsFor(new DateRange(from, to), hostId);

        return new ResponseEntity<Collection<ReportDTO>>( reports, HttpStatus.OK);
    }
    //@PreAuthorize("hasAuthority('HOST')")
    @GetMapping(value = "/generate-pdf/date-range/{hostId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ByteArrayResource> generateReportForDateRange(@PathVariable("hostId") Long hostId,
                                                                        @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date from,
                                                                        @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date to) throws JRException, FileNotFoundException {
        List<ReportDTO> reports = reservationService.getReportsFor(new DateRange(from, to), hostId);
        //return new ResponseEntity<Collection<ReportDTO>>( reports, HttpStatus.OK);

        byte[] reportContent = jasperReportService.getDateRangeReport(reports, Helper.dateToString(from), Helper.dateToString(to));

        ByteArrayResource resource = new ByteArrayResource(reportContent);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("report.pdf")
                                .build().toString())
                .body(resource);
    }

    @PreAuthorize("hasAuthority('HOST')")
    @GetMapping(value = "/accommodation/{hostId}/{accommodationId}/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReportDTO>> getReportForAccommodation(@PathVariable("hostId") Long hostId,
                                                                                       @PathVariable("accommodationId") Long accommodationId,
                                                                                       @PathVariable("year") int year) {
        HashMap<Integer,ReportDTO> reports = reservationService.getReportsFor(year, accommodationId, hostId);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        for (Integer key: reports.keySet()){
            ReportDTO r = reports.get(key);
            r.setMonth(key);
            reportDTOS.add(r);
        }
        return new ResponseEntity<Collection<ReportDTO>>(reportDTOS,HttpStatus.OK);
    }
    @GetMapping(value = "/generate-pdf/accommodation/{hostId}/{accommodationId}/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ByteArrayResource> generateReportForAccommodation(@PathVariable("hostId") Long hostId,
                                                                                @PathVariable("accommodationId") Long accommodationId,
                                                                                @PathVariable("year") int year) throws JRException, FileNotFoundException {
        HashMap<Integer,ReportDTO> reports = reservationService.getReportsFor(year, accommodationId, hostId);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        for (Integer key: reports.keySet()){
            ReportDTO r = reports.get(key);
            r.setMonth(key);
            reportDTOS.add(r);
        }
        byte[] reportContent = jasperReportService.getMonthlyReport(reportDTOS, accommodationId.toString(), String.valueOf(year));

        ByteArrayResource resource = new ByteArrayResource(reportContent);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("report.pdf")
                                .build().toString())
                .body(resource);    }
}
