package onetoone.Chats;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "conversations")
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username1;

    @Column
    private String username2;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Message> messages;

    public Conversation(){};

    public Conversation(String username1, String username2){
        this.username1 = username1;
        this.username2 = username2;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername1(){
        return username1;
    }

    public void setUsername1(String username1){
        this.username1 = username1;
    }

    public String getUsername2(){
        return username2;
    }

    public void setUsername2(String username2){
        this.username2 = username2;
    }

    public List<Message> getMessages(){
        return messages;
    }

    public void setMessages(List<Message> messages){
        this.messages = messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
