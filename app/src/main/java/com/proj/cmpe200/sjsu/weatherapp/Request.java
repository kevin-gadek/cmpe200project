package com.proj.cmpe200.sjsu.weatherapp;

import com.proj.cmpe200.sjsu.weatherapp.model.BaseModel;

public interface Request {
    void onComplete(BaseModel model);
    void onError(BaseModel model);
}
