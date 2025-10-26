//package kr.co.nextplayer.next.lib.common.operation;
//
//import kr.co.nextplayer.next.lib.common.message.ApiResult;
//import kr.co.nextplayer.next.lib.common.message.SMSOperation;
//import kr.co.nextplayer.next.lib.common.property.SMSProperty;
//import kr.co.nextplayer.next.lib.common.property.SMSRecipient;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//@SpringBootTest
//@ActiveProfiles("alpha")
//public class SMSOperationTest {
//
//    @Autowired
//    SMSOperation smsOperation;
//
//    @Test
//    public void sendSMSTest() {
//        HashMap<String, String> templateParameter = new HashMap<>();
//        templateParameter.put("object1", "value");
//        SMSRecipient smsRecipient = SMSRecipient.builder().recipientNo("123").countryCode("aa").templateParameter(templateParameter).build();
//        SMSRecipient smsRecipient2 = SMSRecipient.builder().recipientNo("456").countryCode("bb").templateParameter(templateParameter).build();
//        ArrayList<SMSRecipient> recipientList = new ArrayList<>();
//        recipientList.add(smsRecipient);
//        recipientList.add(smsRecipient2);
//        SMSProperty smsProperty = SMSProperty.builder().templatedId("1").recipientList(recipientList).build();
//        HashMap<String, ApiResult> stringApiResultHashMap = smsOperation.intervalSendSms(2, 2, smsProperty);
//        System.out.println(stringApiResultHashMap);
//    }
//}
