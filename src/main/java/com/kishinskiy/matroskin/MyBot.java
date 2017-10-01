
package com.kishinskiy.matroskin;

import java.util.Comparator;
import java.util.List;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.ChatPhoto;
import org.telegram.telegrambots.api.objects.Location;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;





public class MyBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return botUsername;
    }
    
    @Override
    public String getBotToken() {
       return botToken ; 
    }

    @Override
    public void onUpdateReceived(Update update) {
 
       if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().hasPhoto()){
           String message_text = update.getMessage().getText();
           long chat_id = update.getMessage().getChatId();
          
           
           
           switch (message_text) {
                        case "/start":
                        message_text = "Привет! Для работы с ботом вводите команды через /  \n"
                         + "Например: \n"
                         + "/help\n"
                         + "/start\n"
                         + "/hello\n";
                            break;
                        case "/help":
                         message_text= "Список доступных команд: \n"
                                    + "/help - выведет этот текст \n"
                                    + "/start - Выведет приветствие."
                                    + "/name - Отобразит ваше имя";
                                
                            break;
                            
                        case "/name":
                          
                            
                          String user_first_name = update.getMessage().getChat().getFirstName();
                          String user_last_name = update.getMessage().getChat().getLastName();
                          String user_username = update.getMessage().getChat().getUserName();
                         // File user_photo = update.getMessage().getChat().getPhoto();
                          long user_id = update.getMessage().getChat().getId();
                          
                        
                          
                          SendPhoto msg = new SendPhoto()
                    .setChatId(chat_id)
                  //  .setNewPhoto(user_photo)
                    ;
                    
                          
                          message_text= user_first_name + " \n"
                                  + user_last_name + " \n"
                                  + user_username + " \n"
                                  + user_id;
                          break;
                           
                        default:
                           message_text= "Неизвестная команда!\n"
                                    + "введите /help, чтобы отобразить список доступных команд.  ";
                            break;
                    }

          SendMessage message = new SendMessage() 
                .setChatId(chat_id)
                .setText(message_text);
           
           try{
               sendMessage(message);
           }catch (TelegramApiException e){
               e.printStackTrace();
           }
       }else if(update.hasMessage() && update.getMessage().hasPhoto()){
          long chat_id = update.getMessage().getChatId();
          
          List<PhotoSize> photos = update.getMessage().getPhoto();
          
          String f_id = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();
          
          int f_width = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getWidth();
          
          int f_height = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getHeight();
          String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: " + Integer.toString(f_height);
    SendPhoto msg = new SendPhoto()
                    .setChatId(chat_id)
                    .setPhoto(f_id)
                    .setCaption(caption);
    try {
        sendPhoto(msg); 
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }
          
       }else if (update.hasMessage() && update.getMessage().hasLocation()){
            long chat_id = update.getMessage().getChatId();
            
           Location location = update.getMessage().getLocation();
           
           Float myLongitude = location.getLongitude();
           Float MyLatitude = location.getLatitude();
          
           SendMessage msg = new SendMessage(chat_id, "you here! \n" + "Latitude = " + MyLatitude +"\nLongitude = " +myLongitude  );
           
           try {
               sendMessage(msg);
           } catch (TelegramApiException e){
               e.printStackTrace();
           }
       }
       
       
       
    }

   
}
