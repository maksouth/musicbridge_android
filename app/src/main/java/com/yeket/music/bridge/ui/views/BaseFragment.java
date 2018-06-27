package com.yeket.music.bridge.ui.views;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements BaseContract.View{

    private ProgressDialog progressDialog;

    @Override
    public void showError(@StringRes int messageResourceId) {
        Snackbar.make(getRootLayout(), getString(messageResourceId), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(String message) {
        Snackbar.make(getRootLayout(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar(@StringRes int messageResourceId) {
        if(progressDialog != null){
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(messageResourceId));
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }

        progressDialog = null;
    }

    protected abstract ViewGroup getRootLayout();
}
