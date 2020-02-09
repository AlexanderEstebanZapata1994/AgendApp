package com.example.agendapp.ui.saveSDCard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SaveSDCardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SaveSDCardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sdCard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}