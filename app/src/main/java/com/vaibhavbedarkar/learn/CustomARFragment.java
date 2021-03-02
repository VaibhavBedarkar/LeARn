package com.vaibhavbedarkar.learn;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class CustomARFragment extends ArFragment {

    @Override
    protected Config getSessionConfiguration(Session session) {
        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        ArActivity arActivity = (ArActivity) getActivity();
        assert arActivity != null;
        arActivity.loadDB(session,config);
        this.getArSceneView().setupSession(session);
        return config;
    }
}
