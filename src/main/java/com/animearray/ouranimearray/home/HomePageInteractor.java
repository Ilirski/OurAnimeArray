package com.animearray.ouranimearray.home;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.widgets.DAOs.AccountType;
import com.animearray.ouranimearray.widgets.DAOs.User;
import javafx.beans.binding.Bindings;

public class HomePageInteractor {
    private final HomePageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    public HomePageInteractor(HomePageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
        createAdminBinding();
    }

    public void createAdminBinding() {
        viewModel.adminProperty().bind(Bindings.createBooleanBinding(() -> {
            User currentUser = viewModel.currentUserProperty().get();
            if (currentUser == null) {
                return false;
            }
            return currentUser.accountType() == AccountType.ADMIN;
        }, viewModel.currentUserProperty()));
    }
}
