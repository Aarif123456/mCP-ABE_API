package com.phr.cpabe.Users;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@ControllerAdvice
public class Professional_Portal {
    @GetMapping(value = "/professional_portal")
    public String patientPortal(Model m){
        return "professional_portal";
    }

    /* Will be used for the AJAX call that will happen upon the selection of a patient*/
    @GetMapping(value = "/getPatientFile")
    public @ResponseBody List<String> getPatientFile(@RequestParam("patientID") String patientID){
        /* We will be getting the patients file from AWS */
         List<String> patientFiles = new ArrayList<>();
        switch(patientID){
            case "1":
                patientFiles.add("blood-test.txt");
                patientFiles.add("EKG tests.pdf");
                patientFiles.add("Final exam.pdf");
                break;
            case "2":
                patientFiles.add("sensitive-patient-info.txt");
                patientFiles.add("CAT_test_results.png");
                break;
            case "3":
                patientFiles.add("RealHuman.png");
                patientFiles.add("Pet_scan_result01012020.png");
                patientFiles.add("patella_x_ray.png");
                patientFiles.add("Pet_scan_result01012019.png");
                break;
        }
        return patientFiles;
    }

    @ModelAttribute("patientIDs")
    public List<String> getPatients(Model m){
        /* There is a good chance I will end up creating a class of patient and returning
        * that class instead of integer -> That class will hold things like the patients name
        * - This information will gotten from the Neo4j d*/
        List<String> patients = new ArrayList<>();
        patients.add("1");
        patients.add("2");
        patients.add("3");
        // service.findAll(); // get patient from database
        m.addAttribute("patients",patients);
        return patients;
    }




}
