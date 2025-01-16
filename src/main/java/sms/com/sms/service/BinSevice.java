package sms.com.sms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sms.com.sms.model.BinUser;

import sms.com.sms.repository.BinRepositoty;

@Service
public class BinSevice {
    
    @Autowired
    private BinRepositoty binrepository;
    @Autowired
    private TwilioSMSService twilioSMSService;
    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;
    private final Map<String, BinUser> tempUserStorage = new HashMap<>(); // Temporary user storage

    public boolean isPhoneNumberRegistered(String phonenumber) {
        return binrepository.existsByPhonenumber(phonenumber);
    }

    public void saveTempUser(BinUser user) {
        tempUserStorage.put(user.getPhonenumber(), user); // Temporarily store user
    }

    public BinUser findTempUser(String phonenumber) {
        return tempUserStorage.get(phonenumber);
        
    }

    public BinUser saveUser(BinUser user) {
        return binrepository.save(user);
    }
   
    public void delete (String phonenumber) {
        BinUser details = binrepository.findByPhonenumber(phonenumber);
        if (details == null) {
            throw new IllegalStateException("Number not found");
        }
        binrepository.delete(details);
    }

    public String sendOTP(BinUser user) {
        return twilioSMSService.sendOtpForRegistrations(user);
    }
    /**
     * Update receiver details by phone number.
     */
    
    public BinUser updateReceiversDetails(String phonenumber, String name) {
        BinUser details = binrepository.findByPhonenumber(phonenumber);
        if (details == null) {
            throw new IllegalStateException("Number not found");
        }

        details.setName(name);
        details.setPhonenumber(phonenumber);

        return binrepository.save(details);
    }

    public List<BinUser> getAllDetails() {
        return binrepository.findAll();
    }

    public BinUser getDetails(String phonenumber) {
       BinUser details = binrepository.findByPhonenumber(phonenumber);
        if (details == null) {
            throw new IllegalStateException("Number not found");
        }
        return details;
    }

}
