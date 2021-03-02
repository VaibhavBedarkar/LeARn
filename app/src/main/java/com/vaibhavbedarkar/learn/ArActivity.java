package com.vaibhavbedarkar.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.StringCharacterIterator;
import java.util.Collection;
import java.util.HashMap;

public class ArActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton downloadbtn;
    String model="";
    StringCharacterIterator stringCharacterIterator = new StringCharacterIterator(model);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        downloadbtn = findViewById(R.id.download_btn);




        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdate);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(ArActivity.this,model,Toast.LENGTH_SHORT).show();





    }

    private ModelRenderable renderable;

    private void renderModel(File file) {
        RenderableSource renderableSource = RenderableSource.builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        ModelRenderable
                .builder()
                .setSource(this,renderableSource)
                .setRegistryId(file.getPath())
                .build()
                .thenAccept(modelRenderable -> {
                    Toast.makeText(ArActivity.this,"Model Rendered Successfully!! ",Toast.LENGTH_SHORT).show();
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
        imageToModel.put("animalcell.jpg", "Animal cell.glb");
        imageToModel.put("butterfly.png", "Monarch butterfly.glb");
        imageToModel.put("cat.jpg", "Cat.glb");
        imageToModel.put("humanheart.jpg", "Beating heart.glb");
        imageToModel.put("parrot.jpg", "Parrot.glb");
        imageToModel.put("trex.jpg", "Tyrannosaurus Rex.glb");




        for(AugmentedImage image: images){
            if(image.getTrackingMethod() == AugmentedImage.TrackingMethod.FULL_TRACKING){
                if(imageToModel.get(image.getName())!= null){
                    model = imageToModel.get(image.getName());
                    stringCharacterIterator.setText(model);
                    Toast.makeText(ArActivity.this,"Click to Download: "+model,Toast.LENGTH_SHORT).show();
                    downloadbtn.setVisibility(View.VISIBLE);

                }






                StorageReference modelRef = storage.getReference().child(model);
                downloadbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            File file = File.createTempFile(model,".glb");
                            modelRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    renderModel(file);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                            anchorNode.setRenderable(renderable);
                            arFragment.getArSceneView().getScene().addChild(anchorNode);
                        });


                    }
                });






            }
        }

    }

    public void loadDB(Session session, Config config){
       InputStream dbStream  =  getResources().openRawResource(R.raw.imglist);
       try {
           AugmentedImageDatabase aid = AugmentedImageDatabase.deserialize(session,dbStream);
           config.setAugmentedImageDatabase(aid);
       } catch (IOException e) {
           e.printStackTrace();
       }

   }
}