package cn.itcast.core.pojo.entity;

import java.io.Serializable;

public class ActiveUsers implements Serializable {

    private int allUsers;
    private int activeUsers;
    private int untiveUsers;

    public int getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(int allUsers) {
        this.allUsers = allUsers;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public int getUntiveUsers() {
        return untiveUsers;
    }

    public void setUntiveUsers(int untiveUsers) {
        this.untiveUsers = untiveUsers;
    }
}
