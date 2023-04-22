package com.animearray.ouranimearray.loginregister;

import animatefx.animation.FadeIn;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.function.Consumer;

import static com.animearray.ouranimearray.widgets.Widgets.*;

public class LoginRegisterPageViewBuilder implements Builder<Region> {
    private final LoginRegisterPageModel model;
    private final Consumer<Runnable> verifyUser;
    private final Consumer<Runnable> registerUser;

    public LoginRegisterPageViewBuilder(LoginRegisterPageModel model, Consumer<Runnable> verifyUser, Consumer<Runnable> registerUser) {
        this.model = model;
        this.verifyUser = verifyUser;
        this.registerUser = registerUser;
    }

    @Override
    public Region build() {
        MigPane loginRegisterPane = createLoginRegisterPane();

        MigPane loginPane = setupLoginPane(verifyUser);
        MigPane registerPane = setupRegisterPane(registerUser);

        setupPaneLinks(loginPane, registerPane);

        loginRegisterPane.add(loginPane, new CC().hideMode(3));
        loginRegisterPane.add(registerPane, new CC().hideMode(3));

        return loginRegisterPane;
    }

    private MigPane createLoginRegisterPane() {
        return new MigPane(new LC().align("center", "center"));
    }

    private void setupPaneLinks(MigPane loginPane, MigPane registerPane) {
        var registerPaneLink = createPaneLink("Don't have an account?");
        registerPaneLink.setOnAction(event -> {
            model.setRegisterPageVisible(true);
            model.setLoginPageVisible(false);
            new FadeIn(registerPane).play();
        });
        registerPane.visibleProperty().bind(model.registerPageVisibleProperty());

        var loginPaneLink = createPaneLink("Already have an account?");
        loginPaneLink.setOnAction(event -> {
            model.setRegisterPageVisible(false);
            model.setLoginPageVisible(true);
            new FadeIn(loginPane).play();
        });
        loginPane.visibleProperty().bind(model.loginPageVisibleProperty());

        loginPane.add(registerPaneLink, new CC().grow());
        registerPane.add(loginPaneLink, new CC().grow());
    }

    private MigPane setupLoginPane(Consumer<Runnable> userFetcher) {
        MigPane loginPane = createMigPaneWithCenteredAlignment();

        Label validationLabel = createValidationLabel(model.loginErrorMessageProperty());
        MFXTextField usernameField = createUsernameField(model.usernameLoginProperty());
        MFXPasswordField passwordField = createPasswordField(model.passwordLoginProperty());

        MFXButton loginButton = createLoginButton(usernameField, passwordField, userFetcher);

        addLoginPaneComponents(loginPane, usernameField, passwordField, loginButton, validationLabel);

        return loginPane;
    }

    private MigPane createMigPaneWithCenteredAlignment() {
        return new MigPane(new LC().align("center", "center").wrap());
    }

    private MFXTextField createUsernameField(javafx.beans.property.StringProperty usernameProperty) {
        MFXTextField usernameField = new MFXTextField();
        usernameField.setFloatingText("Username");
        usernameField.textProperty().bindBidirectional(usernameProperty);

        return usernameField;
    }

    private MFXPasswordField createPasswordField(javafx.beans.property.StringProperty passwordProperty) {
        MFXPasswordField passwordField = new MFXPasswordField();
        passwordField.setFloatingText("Password");
        passwordField.textProperty().bindBidirectional(passwordProperty);
        return passwordField;
    }

    private MFXButton createLoginButton(MFXTextField usernameField, MFXPasswordField passwordField, Consumer<Runnable> userFetcher) {
        MFXButton loginButton = new MFXButton("Login");

        var isInvalid = EasyBind.combine(usernameField.textProperty(), passwordField.textProperty(),
                (username, password) -> username.isEmpty() || password.isEmpty());
        BooleanProperty isFetchingUser = new SimpleBooleanProperty(false);

        var disableRegisterButtonBinding = EasyBind.combine(isInvalid, isFetchingUser,
                (invalid, fetching) -> invalid || fetching);

        loginButton.disableProperty().bind(disableRegisterButtonBinding);

        loginButton.setOnAction(event -> {
            isFetchingUser.set(true);
            userFetcher.accept(() -> {
                isFetchingUser.set(false);
                if (!model.isLoggedIn()) {
                    model.setLoginErrorMessage("Wrong username or password, please try again");
                }
            });
        });

        return loginButton;
    }

    private void addLoginPaneComponents(MigPane loginPane, MFXTextField usernameField, MFXPasswordField passwordField, MFXButton loginButton, Label validationLabel) {
        loginPane.add(usernameField, new CC().sizeGroup("login").grow().width("100mm"));
        loginPane.add(passwordField, new CC().sizeGroup("login").grow());
        loginPane.add(loginButton, new CC().grow());
        loginPane.add(validationLabel, new CC().maxWidth("100mm").alignX("center").hideMode(3));
    }

    private MigPane setupRegisterPane(Consumer<Runnable> userFetcher) {
        MigPane registerPane = createMigPaneWithCenteredAlignment();

        Label validationLabel = createValidationLabel(model.registerErrorMessageProperty());
        MFXTextField usernameField = createRegisterUsernameField();
        usernameField.textProperty().bindBidirectional(model.usernameRegisterProperty());
        addValidationListeners(model.registerErrorMessageProperty(), usernameField);

        MFXPasswordField passwordField = createRegisterPasswordField();
        passwordField.textProperty().bindBidirectional(model.passwordRegisterProperty());
        addValidationListeners(model.registerErrorMessageProperty(), passwordField);

        MFXButton registerButton = createRegisterButton(usernameField, passwordField, userFetcher);

        addRegisterPaneComponents(registerPane, usernameField, passwordField, registerButton, validationLabel);

        return registerPane;
    }

    private MFXButton createRegisterButton(MFXTextField usernameField, MFXPasswordField passwordField, Consumer<Runnable> userFetcher) {
        MFXButton registerButton = new MFXButton("Register");

        var isInvalid = EasyBind.combine(usernameField.getValidator().validProperty(), passwordField.getValidator().validProperty(),
                (username, password) -> !username || !password);

        BooleanProperty isFetchingUser = new SimpleBooleanProperty(false);

        var disableRegisterButtonBinding = EasyBind.combine(isInvalid, isFetchingUser,
                (invalid, fetching) -> invalid || fetching);

        registerButton.disableProperty().bind(disableRegisterButtonBinding);

        registerButton.setOnAction(event -> {
            isFetchingUser.set(true);
            userFetcher.accept(() -> {
                isFetchingUser.set(false);
            });
        });

        return registerButton;
    }

    private void addRegisterPaneComponents(MigPane registerPane, MFXTextField usernameField, MFXPasswordField passwordField, MFXButton registerButton, Label validationLabel) {
        registerPane.add(usernameField, new CC().sizeGroup("login").grow().width("100mm"));
        registerPane.add(passwordField, new CC().sizeGroup("login").grow());
        registerPane.add(registerButton, new CC().grow());
        registerPane.add(validationLabel, new CC().maxWidth("100mm").alignX("center").hideMode(3));
    }
}