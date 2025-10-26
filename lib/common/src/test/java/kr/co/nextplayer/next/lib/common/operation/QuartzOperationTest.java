//package kr.co.nextplayer.next.lib.common.operation;
//
//import kr.co.nextplayer.next.lib.common.property.EmailProperty;
//import kr.co.nextplayer.next.lib.common.property.ReceiveMailAddress;
//import kr.co.nextplayer.next.lib.common.property.TaskDefineProperty;
//import org.junit.jupiter.api.Test;
//import org.quartz.JobDataMap;
//import org.quartz.JobKey;
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
//public class QuartzOperationTest {
//
//    @Autowired
//    private QuartzOperation quartzOperation;
//
//    @Test
//    public void modifyScheduleJobTest() {
//        HashMap<String, Object> templateParameter = new HashMap<>();
//        templateParameter.put("object1", "value");
//        List<ReceiveMailAddress> receiveMailAddressList = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            ReceiveMailAddress receiveMailAddress = ReceiveMailAddress.builder().receiveMailAddr("receiveUpdate" + i).receiveName("receiveNameUpdate" + i).receiveType("type1").build();
//            receiveMailAddressList.add(receiveMailAddress);
//        }
//        EmailProperty emailProperty = EmailProperty.builder()
//                .templateId("123")
//                .senderAddress("addreissUpdate")
//                .senderName("nameUpdate")
//                .templateParameter(templateParameter)
//                .receiverList(receiveMailAddressList)
//                .build();
//        JobDataMap jobDataMap = new JobDataMap();
//        jobDataMap.put("emailProperty", emailProperty);
//
//        JobKey jobKey = new JobKey("sendEmailTest10", "email");
//
//        TaskDefineProperty taskDefineProperty = TaskDefineProperty
//                .builder()
//                .jobKey(jobKey)
//                .jobDataMap(jobDataMap)
//                .cronExpression("0 25 8 29 3 ? 2022")
//                .build();
//
//        assert quartzOperation.modifyScheduleJob(taskDefineProperty);
//    }
//
//}
