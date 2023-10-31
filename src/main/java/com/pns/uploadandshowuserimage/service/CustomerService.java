package com.pns.uploadandshowuserimage.service;

import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.amazonaws.util.StringUtils;
import com.pns.uploadandshowuserimage.model.Customer;
import com.pns.uploadandshowuserimage.model.S3Buckets;
import com.pns.uploadandshowuserimage.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CustomerService {
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final CustomerRepository customerRepository;

    public CustomerService(S3Service s3Service, S3Buckets s3Buckets, CustomerRepository customerRepository) {
        this.s3Service = s3Service;
        this.s3Buckets = s3Buckets;
        this.customerRepository = customerRepository;
    }

    public void uploadCustomerProfileImage(Integer customerId, MultipartFile image) {
        checkIfCustomerExistsOrThrow(customerId);
        String profileImageId = UUID.randomUUID().toString();
        try{
            s3Service.putObject(
                    s3Buckets.getCustomer(),
                        "profile-images/%s/%s".formatted(customerId, profileImageId),
                    image.getBytes()
            );
        } catch (IOException ioException){
            throw new RuntimeException("failed to upload the image", ioException);
        }
        customerRepository.updateProfileImageId(profileImageId, customerId);
    }

    public byte[] getCustomerProfileImage(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow();
        if(StringUtils.isNullOrEmpty(customer.getProfileImageId())){
            throw new ResourceNotFoundException(
                    "customer with id [%s] profile image not found".formatted(customerId)
            );
        }


        return s3Service.getObject( // return the profile image
                s3Buckets.getCustomer(),
                "profile-images/%s/%s".formatted(customerId, customer.getProfileImageId()));
    }

    private void checkIfCustomerExistsOrThrow(Integer customerId){
        if(!customerRepository.existsCustomerById(customerId)){
            throw new ResourceNotFoundException(
                    "customer with id [%s] not found".formatted(customerId)
            );
        }
    }
}
