package dk.au.mad21spring.appproject.group6.fragments.wrapper;

import dk.au.mad21spring.appproject.group6.viewmodels.wrapper.WrapperViewModel;

public interface WrapperInterface {
    void onBeverageSelected(int position);
    void queryList(String query);
    void switchFragment(WrapperViewModel.SelectedFragment fragmentSelection);
}
