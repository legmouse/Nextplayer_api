//package kr.co.nextplayer.next.lib.common.operation;
//
//import kr.co.nextplayer.next.lib.common.message.ApiResult;
//import kr.co.nextplayer.next.lib.common.message.EmailOperation;
//import kr.co.nextplayer.next.lib.common.property.EmailProperty;
//import kr.co.nextplayer.next.lib.common.property.ReceiveMailAddress;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//@SpringBootTest
//@ActiveProfiles("alpha")
//public class EmailOperationTest {
//
//    @Autowired
//    private EmailOperation emailOperation;
//
//    @Test
//    public void SendMailTest() {
//        HashMap<String, Object> templateParameter = new HashMap<>();
//        templateParameter.put("object1", "value");
//        List<ReceiveMailAddress> receiveMailAddressList = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            ReceiveMailAddress receiveMailAddress = ReceiveMailAddress.builder().receiveMailAddr("receive" + i).receiveName("receiveName" + i).receiveType("type1").build();
//            receiveMailAddressList.add(receiveMailAddress);
//        }
//        EmailProperty emailProperty = EmailProperty.builder()
//                .templateId("123")
//                .senderAddress("addreiss")
//                .senderName("aa")
//                .templateParameter(templateParameter)
//                .receiverList(receiveMailAddressList)
//                .build();
//        HashMap<String, ApiResult> stringApiResultHashMap = emailOperation.intervalSendEmail(2, 2, emailProperty);
//        System.out.println(stringApiResultHashMap);
//    }
//
//    @Test
//    public void reqularSendEmailTest() {
//        HashMap<String, Object> templateParameter = new HashMap<>();
//        templateParameter.put("object1", "value");
//        List<ReceiveMailAddress> receiveMailAddressList = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            ReceiveMailAddress receiveMailAddress = ReceiveMailAddress.builder().receiveMailAddr("receive" + i).receiveName("receiveName" + i).receiveType("type1").build();
//            receiveMailAddressList.add(receiveMailAddress);
//        }
//        EmailProperty emailProperty = EmailProperty.builder()
//                .templateId("123")
//                .senderAddress("address")
//                .senderName("aa")
//                .templateParameter(templateParameter)
//                .receiverList(receiveMailAddressList)
//                .build();
//
//        assert emailOperation.timingSendEmail(emailProperty, "0 32 16 25 3 ? 2022", "sendEmailTest2", "emailGroup", "Send emails regularly");
//    }
//}
