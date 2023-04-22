package com.animearray.ouranimearray.loginregister;

import com.animearray.ouranimearray.model.User;
import com.tobiasdiez.easybind.EasyBind;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;

public class LoginRegisterPageModel {
    private final BooleanProperty registerPageVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty loginPageVisible = new SimpleBooleanProperty(true);
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private final StringProperty usernameRegister = new SimpleStringProperty("");
    private final StringProperty passwordRegister = new SimpleStringProperty("");
    private final StringProperty usernameLogin = new SimpleStringProperty("");
    private final StringProperty passwordLogin = new SimpleStringProperty("");
    private final StringProperty loginErrorMessage = new SimpleStringProperty("");
    private final StringProperty registerErrorMessage = new SimpleStringProperty("");
    private final BooleanBinding loggedIn = EasyBind.wrapNullable(currentUser).isPresent();

    public Boolean isLoggedIn() {
        return loggedIn.get();
    }

    public BooleanBinding loggedInProperty() {
        return loggedIn;
    }

    public User getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser.set(currentUser);
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public boolean isRegisterPageVisible() {
        return registerPageVisible.get();
    }

    public void setRegisterPageVisible(boolean registerPageVisible) {
        this.registerPageVisible.set(registerPageVisible);
    }

    public BooleanProperty registerPageVisibleProperty() {
        return registerPageVisible;
    }

    public boolean isLoginPageVisible() {
        return loginPageVisible.get();
    }

    public void setLoginPageVisible(boolean loginPageVisible) {
        this.loginPageVisible.set(loginPageVisible);
    }

    public BooleanProperty loginPageVisibleProperty() {
        return loginPageVisible;
    }

    public String getLoginErrorMessage() {
        return loginErrorMessage.get();
    }

    public void setLoginErrorMessage(String loginErrorMessage) {
        this.loginErrorMessage.set(loginErrorMessage);
    }

    public StringProperty loginErrorMessageProperty() {
        return loginErrorMessage;
    }

    public String getRegisterErrorMessage() {
        return registerErrorMessage.get();
    }

    public void setRegisterErrorMessage(String registerErrorMessage) {
        this.registerErrorMessage.set(registerErrorMessage);
    }

    public StringProperty registerErrorMessageProperty() {
        return registerErrorMessage;
    }

    public String getUsernameRegister() {
        return usernameRegister.get();
    }

    public void setUsernameRegister(String usernameRegister) {
        this.usernameRegister.set(usernameRegister);
    }

    public StringProperty usernameRegisterProperty() {
        return usernameRegister;
    }

    public String getPasswordRegister() {
        return passwordRegister.get();
    }

    public void setPasswordRegister(String passwordRegister) {
        this.passwordRegister.set(passwordRegister);
    }

    public StringProperty passwordRegisterProperty() {
        return passwordRegister;
    }

    public String getUsernameLogin() {
        return usernameLogin.get();
    }

    public void setUsernameLogin(String usernameLogin) {
        this.usernameLogin.set(usernameLogin);
    }

    public StringProperty usernameLoginProperty() {
        return usernameLogin;
    }

    public String getPasswordLogin() {
        return passwordLogin.get();
    }

    public void setPasswordLogin(String passwordLogin) {
        this.passwordLogin.set(passwordLogin);
    }

    public StringProperty passwordLoginProperty() {
        return passwordLogin;
    }

}
