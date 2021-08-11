package com.example.kuime;

import org.json.JSONException;

public interface IaResultHandler {
    void onResult(CaResult Result) throws JSONException;
}
