package com.animearray.ouranimearray.widgets;

import com.animearray.ouranimearray.widgets.DAOs.UserList;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class UserListProperty extends ObjectPropertyBase<UserList> {
    private final StringProperty listId;
    private final StringProperty userId;
    private final StringProperty listName;
    private final StringProperty description;
    private final ObjectProperty<LocalDateTime> createdDate;

    public UserListProperty(UserList userList) {
        set(userList);

        this.listId = new SimpleStringProperty(userList != null ? userList.listId() : "");
        this.userId = new SimpleStringProperty(userList != null ? userList.userId() : "");
        this.listName = new SimpleStringProperty(userList != null ? userList.name() : "");
        this.description = new SimpleStringProperty(userList != null ? userList.description() : "");
        this.createdDate = new SimpleObjectProperty<>(userList != null ? userList.createdDate() : null);

        // Add a listener such that when the current UserListProperty changes, its properties are updated
        this.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.listId.set(newValue.listId());
                this.userId.set(newValue.userId());
                this.listName.set(newValue.name());
                this.description.set(newValue.description() != null ? newValue.description() : "");
                this.createdDate.set(newValue.createdDate());
            } else {
                this.listId.set("");
                this.userId.set("");
                this.listName.set("");
                this.description.set("");
                this.createdDate.set(null);
            }
        });
    }

    public UserListProperty() {
        this(null);
    }

    public String getListId() {
        return listId.get();
    }

    public void setListId(String listId) {
        this.listId.set(listId);
    }

    public StringProperty listIdProperty() {
        return listId;
    }

    public String getUserId() {
        return userId.get();
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public String getListName() {
        return listName.get();
    }

    public void setListName(String listName) {
        this.listName.set(listName);
    }

    public StringProperty listNameProperty() {
        return listName;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate.get();
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate.set(createdDate);
    }

    public ObjectProperty<LocalDateTime> createdDateProperty() {
        return createdDate;
    }

    @Override
    public String toString() {
        return "UserListProperty{" +
                "listId=" + listId +
                ", userId=" + userId +
                ", listName=" + listName +
                ", description=" + description +
                ", createdDate=" + createdDate +
                '}';
    }


    @Override
    public Object getBean() {
        return this;
    }

    @Override
    public String getName() {
        return "UserListProperty";
    }
}
