package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.ArFragment;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable renderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load 3D Model Resources
        ModelRenderable.builder()
                .setSource(
                        this,
                        Uri.parse("file:///android_asset/cat.glb")
                )
                .build()
                .thenAccept(modelRenderable -> {
                    // Store the model renderable for later use
                    renderable = modelRenderable;
                })
                .exceptionally(throwable -> {
                    // Handle errors here
                    return null;
                });

        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
            anchorNode.setParent(arFragment.getArSceneView().getScene());

            TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
            transformableNode.setRenderable(renderable);
            transformableNode.setParent(anchorNode);

            arFragment.getArSceneView().getScene().addChild(anchorNode);
            transformableNode.select();
        });
    }
}
