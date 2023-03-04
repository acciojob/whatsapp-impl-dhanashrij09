package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {
    @Autowired
            WhatsappRepository whatsappRepository;

    public static String createUser(String name,String mobile)  throws Exception {
        return WhatsappRepository.createUser(name,mobile);
    }

    public static Group createGroup(List<User> users){
       return  WhatsappRepository.createGroup(users);
    }

    public static int createMessage(String content){

        return WhatsappRepository.createMessage(content);
    }

    public static int sendMessage(Message message, User sender, Group group)  throws Exception{
        return WhatsappRepository.sendMessage(message,sender,group);
    }

    public static String changeAdmin(User approver, User user, Group group) throws Exception{
      return  WhatsappRepository.changeAdmin(approver, user, group);
    }

   public static int removeUser(User user) throws Exception{
       return WhatsappRepository.removeUser(user);
    }

   public static String findMessage(Date start, Date end, int K) throws Exception{
       return WhatsappRepository.findMessage(start,end,K);
   }
}
