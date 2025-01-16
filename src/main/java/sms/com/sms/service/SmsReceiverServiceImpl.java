package sms.com.sms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sms.com.sms.exception.ResourceNotFoundException;
import sms.com.sms.model.ReceiverDetails;
import sms.com.sms.repository.ReceiverRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SmsReceiverServiceImpl implements SmsReceiverService {
    @Autowired
    private ReceiverRepository repository;
    @Autowired
 
    private TwilioSMSService twilioSMSService;
    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;
    private final Map<String, ReceiverDetails> tempUserStorage = new HashMap<>(); // Temporary user storage

    public boolean isPhoneNumberRegistered(String phonenumber) {
        return repository.existsByPhonenumber(phonenumber);
    }

    public void saveTempUser(ReceiverDetails user) {
        tempUserStorage.put(user.getPhonenumber(), user); // Temporarily store user
    }

    public ReceiverDetails findTempUser(String phonenumber) {
        return tempUserStorage.get(phonenumber);
    }

    public ReceiverDetails saveUser(ReceiverDetails user) {
        return repository.save(user);
    }
    @Override
    public void deletes(String phonenumber) {
        ReceiverDetails details = repository.findByPhonenumber(phonenumber);
        if (details == null) {
            throw new IllegalStateException("Number not found");
        }
        repository.delete(details);
    }

    public String sendOTP(ReceiverDetails user) {
    try {
        return twilioSMSService.sendOtpForRegistration(user);
    } catch (Exception e) {
        System.out.println("error"+ e);
        return "Failed to send OTP to " + user.getPhonenumber() + ": " + e.getMessage();
    }
       
    }
    /**
     * Update receiver details by phone number.
     */
    @Override
    public ReceiverDetails updateReceiversDetails(String phonenumber, String name) {
        ReceiverDetails details = repository.findByPhonenumber(phonenumber);
        if (details == null) {
            throw new IllegalStateException("Number not found");
        }

        details.setName(name);
        details.setPhonenumber(phonenumber);

        return repository.save(details);
    }

    /**
     * Update receiver details using the phone number and a new details object.
     */
    @Override
    public ReceiverDetails UpdataProductPh(String phonenumber, ReceiverDetails newDetails) {
        ReceiverDetails existingDetails = repository.findByPhonenumber(phonenumber);
        if (existingDetails == null) {
            throw new IllegalStateException("Number not found");
        }

        existingDetails.setName(newDetails.getName());
        existingDetails.setPhonenumber(newDetails.getPhonenumber());

        return repository.save(existingDetails);
    }

    /**
     * Retrieve details of a specific receiver by phone number.
     */
    @Override
    public ReceiverDetails getDetails(String phonenumber) {
        ReceiverDetails details = repository.findByPhonenumber(phonenumber);
        if (details == null) {
            throw new IllegalStateException("Number not found");
        }
        return details;
    }

    /**
     * Update receiver details using ID and a new details object.
     */
    @Override
    public ReceiverDetails UpdataProduct(long id, ReceiverDetails newDetails) {
        ReceiverDetails existingDetails = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found with id: " + id));

        existingDetails.setName(newDetails.getName());
        existingDetails.setPhonenumber(newDetails.getPhonenumber());

        return repository.save(existingDetails);
    }

    /**
     * Retrieve all receiver details from the database.
     */
    @Override
    public List<ReceiverDetails> getAllDetails() {
        return repository.findAll();
    }

}
