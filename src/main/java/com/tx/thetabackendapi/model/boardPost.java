package com.tx.thetabackendapi.model;
import jakarta.persistence.*;

@Entity
@Table(name = "boardposts")
public class boardPost {
    
    //automatically generate id for testing purposes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idboardposts;
    

    //these four varibles match what the app is expecting
    @Column(name = "title", nullable = false, length = 45)
    private String title;
    
    @Column(name = "body", nullable = false, length = 45)
    private String body;
    
    @Column(name = "author", nullable = false, length = 45)
    private String author;
    
    @Column(name = "date", nullable = false, length = 45)
    private String date;  
    
    //constructors
    public boardPost() {}
    
    public boardPost(String title, String body, String author, String date) {
        //this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.date = date;
    }
    
    //getters and setters

    //public Long getId() {
   //     return id;
   // }
    
   // public void setId(Long id) {
    //    this.id = id;
    //}
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
}