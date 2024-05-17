package coms309;

public class userCreator {
    protected String name;
    protected boolean isUser;
    protected boolean isAdmin;
    protected boolean isBand;

    public userCreator(String name, String userType){
        this.name = name;
        if(userType.contains("admin")){
            isAdmin = true;
            isBand = false;
            isUser = false;
        } else if(userType.contains("band")){
            isBand = true;
            isAdmin = false;
            isUser = false;
        } else {
            isUser = true;
            isBand = false;
            isAdmin = false;
        }
    }

    public String printUserInfo(){
    return "Name: " + name + "\n User: " + isUser + "\n Admin: " + isAdmin + "\n Band:" + isBand;
    }

    public String getUserName() {
        return name;
    }

}
