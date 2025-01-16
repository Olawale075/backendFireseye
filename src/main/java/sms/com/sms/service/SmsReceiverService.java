package sms.com.sms.service;


import java.util.List;


import sms.com.sms.model.ReceiverDetails;

public interface SmsReceiverService {

 // public String saveSmsReceiver(String name, String phonenumber, String otp ) ;
  public String sendOTP(  ReceiverDetails user);
  public ReceiverDetails updateReceiversDetails( String phonenumber, String name);

   public ReceiverDetails getDetails(String phonenumber);
   public List<ReceiverDetails> getAllDetails();
    void deletes(String phonenumder);

    public ReceiverDetails UpdataProductPh(String phonenumber, ReceiverDetails details);

  public  ReceiverDetails UpdataProduct(long id, ReceiverDetails product);

}
