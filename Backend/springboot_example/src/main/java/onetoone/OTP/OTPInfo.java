package onetoone.OTP;

import jakarta.persistence.*;
import onetoone.Users.LoginInfo;


public class OTPInfo {
        private String name;

        private String emailId;

        private String id;

        private String request;

        public OTPInfo(String id, String emailId, String name, String request) {
            this.id = id;
            this.emailId = emailId;
            this.name = name;
            this.request = request;
        }

        public OTPInfo() {
        }

        // =============================== Getters and Setters for each field ================================== //


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
            this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
