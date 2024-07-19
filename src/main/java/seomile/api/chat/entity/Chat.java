package seomile.api.chat.entity;

import jakarta.persistence.*;
import seomile.api.member.entity.Member;

import java.util.Date;

@Entity
@Table(name = "Chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member;

    private String content;
    private Date date;
    private String request;
}