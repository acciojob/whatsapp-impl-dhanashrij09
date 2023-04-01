package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository() {
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public void createUser(String name, String mobile) throws Exception {
        //User newUser = new User(name, mobile);
        if (userMobile.contains(mobile)) {
            throw new Exception("User already exists");
        } else
            userMobile.add(mobile);
        User user = new User(name, mobile);

    }

    public Group createGroup(List<User> users) {
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
        Group group = new Group(groupName,Group.getNumberOfParticipants());
        groupUserMap.put(group, users);
        groupMessageMap.put(group, new ArrayList<>());
        adminMap.put(group, admin);

        return group;
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId, content, new Date());
        senderMap.put(message, null);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
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

    public String changeAdmin(User approver, User user, Group group) throws Exception {
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


    public int removeUser(User user) throws Exception {
        if (user.getName() == null) {
            throw new IllegalArgumentException("User not found");
        }
//        Group group = user.getGroup();
//        if (adminMap.get(group).equals(user)) {
//            throw new IllegalArgumentException("Cannot remove admin");
//        }
//        userList.remove(user);
//        return userList.size();
//    }

       for(User users :  senderMap.values()) {
            if (users == null) {
               throw new IllegalArgumentException("User not found");
           } else {
                senderMap.remove(users);

            }
       }
            for (User userss : adminMap.values()) {
               if (userss == null) {
                   throw new IllegalArgumentException("User not found");
               } else
                   senderMap.remove(userss);

            }
            Collection<List<User>> userList = groupUserMap.values();
           if (userList.size() > 2) {
               userList.remove(user);
           }
         return userList.size();
        }

    public  String findMessage(Date start, Date end, int K) throws Exception{
        List<Message> messages = new ArrayList<>();
       for (List<Message> groupMessages : groupMessageMap.values()) {for (Message message : groupMessages) {
               if (message.getTimestamp().after(start) && message.getTimestamp().before(end)) {messages.add(message);
              }
           }
       }
      if (messages.size() < K) {
          throw new Exception("K is greater than the number of messages");
       }

      return messages.toString();
    }
}
