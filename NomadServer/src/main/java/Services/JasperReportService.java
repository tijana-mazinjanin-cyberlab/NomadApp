package Services;

import DTO.ReportDTO;
import Repositories.IRepository;
import model.Amenity;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class JasperReportService {
    public byte[] getDateRangeReport(List<ReportDTO> items, String from, String to) throws FileNotFoundException, JRException {

        File file = ResourceUtils.getFile("src/main/resources/report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        //Set report data
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Report for all your accommodations selected date range - " + from +"-"+ to);
        parameters.put("acc", "Accommodation id");
        parameters.put("prof", "Profit");
        parameters.put("res", "Reservations");


        JasperPrint jasperPrint = null;
        byte[] reportContent = new byte[0];

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            //handle exception
        }
        return reportContent;
    }
    public byte[] getMonthlyReport(List<ReportDTO> items, String accommodation, String year) throws FileNotFoundException, JRException {

        File file = ResourceUtils.getFile("src/main/resources/reportMonthly.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        //Set report data
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Monthly report for accommodation id- " + accommodation +" in "+year+".");
        parameters.put("acc", "Month");
        parameters.put("prof", "Profit");
        parameters.put("res", "Reservations");


        JasperPrint jasperPrint = null;
        byte[] reportContent = new byte[0];

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            //handle exception
        }
        return reportContent;
    }
}
