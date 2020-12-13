package com.google.firebase.samples.apps.mlkit.facedetection;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.samples.apps.mlkit.FrameMetadata;
import com.google.firebase.samples.apps.mlkit.GraphicOverlay;
import com.google.firebase.samples.apps.mlkit.MusicActivity;
import com.google.firebase.samples.apps.mlkit.VisionProcessorBase;

import java.io.IOException;
import java.util.List;

/** Face Detector Demo. */
public class FaceDetectionProcessor extends VisionProcessorBase<List<FirebaseVisionFace>> {

  private static final String TAG = "FaceDetectionProcessor";
  Context context;

  public float getVal() {
    return val;
  }

  public void setVal(float val) {
    this.val = val;
  }

  private float val;
  int happCount = 1;
  int sadCount = 1;

  private final FirebaseVisionFaceDetector detector;

  public FaceDetectionProcessor(Context applicationContext) {

    this.context = applicationContext;

    FirebaseVisionFaceDetectorOptions options =
        new FirebaseVisionFaceDetectorOptions.Builder()
            .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .setTrackingEnabled(true)
            .build();

    detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
  }

  @Override
  public void stop() {
    try {
      detector.close();
    } catch (IOException e) {
      Log.e(TAG, "Exception thrown while trying to close Face Detector: " + e);
    }
  }

  @Override
  protected Task<List<FirebaseVisionFace>> detectInImage(FirebaseVisionImage image) {
    return detector.detectInImage(image);
  }

  @Override
  protected void onSuccess(
      @NonNull List<FirebaseVisionFace> faces,
      @NonNull FrameMetadata frameMetadata,
      @NonNull GraphicOverlay graphicOverlay) {
      graphicOverlay.clear();
    for (int i = 0; i < faces.size(); ++i) {
      FirebaseVisionFace face = faces.get(i);
      FaceGraphic faceGraphic = new FaceGraphic(graphicOverlay);
      graphicOverlay.add(faceGraphic);
      faceGraphic.updateFace(face, frameMetadata.getCameraFacing());
      if (face.getSmilingProbability() >0.50){
        happCount++;
        if (happCount>10) {
          stop();
          Intent in = new Intent(context, MusicActivity.class);
          in.putExtra("happiness", face.getSmilingProbability());
          in.putExtra("happyFlag",true);
          context.startActivity(in);
        }
      }else if(face.getSmilingProbability() <0.20){
         sadCount++;
         if (sadCount>10){
           stop();
           Intent in = new Intent(context, MusicActivity.class);
           in.putExtra("happiness", face.getSmilingProbability());
           in.putExtra("happyFlag",false);
           context.startActivity(in);

         }
      }
    }
  }


  @Override
  protected void onFailure(@NonNull Exception e) {
    Log.e(TAG, "Face detection failed " + e);
  }
}
