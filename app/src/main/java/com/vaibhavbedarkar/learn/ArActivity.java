package com.vaibhavbedarkar.learn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.StringCharacterIterator;
import java.util.Collection;
import java.util.HashMap;

import javax.crypto.AEADBadTagException;

public class ArActivity extends AppCompatActivity {
    private ArFragment arFragment;
    String devName = "";
    String model = "";
    StringCharacterIterator stringCharacterIterator = new StringCharacterIterator(model);
    private com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton downloadbtn, close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        downloadbtn = findViewById(R.id.download_btn);
        close_btn = findViewById(R.id.close_btn);

        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdate);


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private ModelRenderable renderable;

    private void renderModel(File file) {
        RenderableSource renderableSource = RenderableSource.builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        ModelRenderable
                .builder()
                .setSource(this, renderableSource)
                .setRegistryId(file.getPath())
                .build()
                .thenAccept(modelRenderable -> {
                    Toast.makeText(ArActivity.this, "Model Rendered Successfully!! ", Toast.LENGTH_SHORT).show();
                    renderable = modelRenderable;
                });

    }

    private void onUpdate(FrameTime frameTime) {
        FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Frame frame = arFragment.getArSceneView().getArFrame();

        Collection<AugmentedImage> images = frame.getUpdatedTrackables(AugmentedImage.class);

        HashMap<String, String> imageToModel = new HashMap<>();
        imageToModel.put("Sun.jpg", "Orbiting solar system.glb");
        imageToModel.put("animalcell.jpg", "Animal Cell Detail.glb");
        imageToModel.put("butterfly.png", "Monarch butterfly.glb");
        imageToModel.put("cat.jpg", "Cat.glb");
        imageToModel.put("humanheart.jpg", "Beating heart.glb");
        imageToModel.put("parrot.jpg", "Parrot.glb");
        imageToModel.put("trex.jpg", "Tyrannosaurus Rex.glb");


        for (AugmentedImage image : images) {
            if (image.getTrackingMethod() == AugmentedImage.TrackingMethod.FULL_TRACKING) {
                if (imageToModel.get(image.getName()) != null) {
                    model = imageToModel.get(image.getName());
                    stringCharacterIterator.setText(model);
                    downloadbtn.setVisibility(View.VISIBLE);
                }

                StorageReference modelRef = storage.getReference().child(model);
                downloadbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadbtn.setVisibility(View.INVISIBLE);
                        try {
                            File file = File.createTempFile(model, ".glb");
                            modelRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    renderModel(file);
                                    Toast.makeText(ArActivity.this, "Asset Downloaded Successfully, Tap to open ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                                    AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                                    anchorNode.setRenderable(renderable);
                                    arFragment.getArSceneView().getScene().addChild(anchorNode);
                                });
                            }
                        }).start();
                    }
                });

            }
        }

    }

    public void loadDB(Session session, Config config) {
        InputStream dbStream = getResources().openRawResource(R.raw.imglist);
        try {
            AugmentedImageDatabase aid = AugmentedImageDatabase.deserialize(session, dbStream);
            config.setAugmentedImageDatabase(aid);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}