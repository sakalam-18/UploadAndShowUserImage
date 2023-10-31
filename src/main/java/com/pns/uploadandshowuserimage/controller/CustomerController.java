package com.pns.uploadandshowuserimage.controller;

import com.pns.uploadandshowuserimage.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("api/v1/customers")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CustomerController {
    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @Transactional
    @PostMapping(
            value = "{customerId}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadCustomerProfileImage(@PathVariable Integer customerId, @RequestParam MultipartFile image) {
        System.out.println("Uploading the image..");
        customerService.uploadCustomerProfileImage(customerId, image);
    }

    @GetMapping(
            value = "{customerId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getCustomerProfileImage(@PathVariable Integer customerId) {
        return customerService.getCustomerProfileImage(customerId);
    }
}
