package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private static HashMap<Group, List<User>> groupUserMap;
    private static HashMap<Group, List<Message>> groupMessageMap;
    private static HashMap<Message, User> senderMap;
    private static HashMap<Group, User> adminMap;
    private static HashSet<String> userMobile;
    private static int customGroupCount;
    private static int messageId;

    public WhatsappRepository() {
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public static String createUser(String name, String mobile)  throws Exception{
        //User newUser = new User(name, mobile);
        if (userMobile.contains(mobile)) {
            throw new IllegalArgumentException("User already exists");
        } else
            userMobile.add(mobile);
        User user = new User(name, mobile);

        return "SUCCESS";
    }

    public static Group createGroup(List<User> users){
        if (users.size() < 2) {
            throw new IllegalArgumentException("A group should have at least 2 users.");
        }
        User admin = users.get(0);
        if (adminMap.containsValue(admin)) {
            throw new IllegalArgumentException("A user can only be an admin of one group.");
        }
        customGroupCount++;
        String groupName;
        if (users.size() == 2) {
            groupName = users.get(1).getName();
        } else {
            groupName = "Group " + customGroupCount;
        }
        Group group = new Group(groupName);
        groupUserMap.put(group, users);
        groupMessageMap.put(group, new ArrayList<>());
        adminMap.put(group, admin);

        return group;
    }

    public static int createMessage(String content){
       //Message message = new Message(content);
      // return messageId++;
        messageId++;
        Message message = new Message(messageId, content, new Date());
        senderMap.put(message, null);
        return messageId;
    }

    public static int sendMessage(Message message, User sender, Group group)  throws Exception{
        if (!groupUserMap.containsKey(group)) {
            throw new IllegalArgumentException("Group does not exist.");
        }
        List<User> userList = groupUserMap.get(group);
        if (!userList.contains(sender)) {
            throw new IllegalArgumentException("Sender is not a member of the group.");
        }
        senderMap.put(message, sender);
        List<Message> messageList = groupMessageMap.get(group);
        messageList.add(message);
        return messageList.size();
    }

    public static String changeAdmin(User approver, User user, Group group) throws Exception{
        if (!groupUserMap.containsKey(group)) {
            throw new IllegalArgumentException("Group does not exist.");
        }
        if (!adminMap.get(group).equals(approver)) {
            throw new IllegalArgumentException("Approver does not have rights");
        }
        List<User> userList = groupUserMap.get(group);
        if (!userList.contains(user)) {
            throw new IllegalArgumentException("Approver does not have rights");
        }
        adminMap.put(group, user);
        return "SUCCESS";
    }

    public static int removeUser(User user) throws Exception{
        if (user.getName()==null){
            throw new IllegalArgumentException("User not found");
        }
        Group group = user.getGroup();
        List<User> userList = groupUserMap.get(group);
        if (adminMap.get(group).equals(user)) {
            throw new IllegalArgumentException("Cannot remove admin");
        }
         userList.remove(user);

         return 0;


//        for(User users :  senderMap.values()) {
//            if (users == null) {
//                throw new IllegalArgumentException("User not found in any group.");
//            } else {
//                senderMap.remove(users);
//
//            }
//        }
//            for (User userss : adminMap.values()) {
//                if (userss == null) {
//                    throw new IllegalArgumentException("User not found in any group.");
//                } else
//                    senderMap.remove(userss);
//
//            }
//            Collection<List<User>> userList = groupUserMap.values();
//            if (userList.size() > 2) {
//                userList.remove(user);
//            }
//
        }

    public  static String findMessage(Date start, Date end, int K) throws Exception{
        List<Message> messages = new ArrayList<>();
        for (List<Message> groupMessages : groupMessageMap.values()) {
            for (Message message : groupMessages) {
                if (message.getTimestamp().after(start) && message.getTimestamp().before(end)) {
                    messages.add(message);
                }
            }
        }
        if (messages.size() < K) {
            throw new Exception("K is greater than the number of messages");
        }

        return messages.toString();
    }
}
