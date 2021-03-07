package com.phr.cpabe.DataOwner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Patient_Portal {
    @GetMapping(value ="/patient_portal")
    public String patient_portal(){
        return "patient_portal";
    }


}
